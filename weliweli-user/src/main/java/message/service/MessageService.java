package message.service;

import admin.dao.UserDao;
import admin.entity.User;
import com.alibaba.fastjson.JSONObject;
import message.dao.MessageContactDao;
import message.dao.MessageContentDao;
import message.dao.MessageRelationDao;
import message.dto.MessageVO;
import message.entity.MessageContact;
import message.entity.MessageContent;
import message.entity.MessageRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import share.Constants;

import java.util.Date;

@Service
public class MessageService {

    @Autowired
    MessageContentDao contentDao;

    @Autowired
    MessageRelationDao relationDao;

    @Autowired
    MessageContactDao contactDao;

    @Autowired
    UserDao userDao;

    @Autowired
    JedisPool redisPool;

    public MessageVO sendMsg(Long senderUid, Long receiverUid, String content, Integer msgType) {
        Date currentDate = new Date();
        //存消息
        MessageContent messageContent = new MessageContent();
        messageContent.setContent(content);
        messageContent.setCreateTime(currentDate);
        messageContent.setSenderId(senderUid);
        messageContent.setMsgType(msgType);
        messageContent.setRecipientId(receiverUid);

        Long mid = contentDao.save(messageContent);
        //存收件人发件人信箱
        MessageRelation messageRelationSender = new MessageRelation(mid, senderUid, receiverUid,
                msgType, currentDate);
        relationDao.save(messageRelationSender);
        MessageRelation messageRelationReceiver = new MessageRelation(mid, senderUid, receiverUid,
                msgType, currentDate);
        relationDao.save(messageRelationReceiver);

        //存发件人最近聊天
        MessageContact senderContact = contactDao.find(senderUid);
        if (senderContact == null) {
            senderContact = new MessageContact(senderUid, receiverUid, mid, msgType,
                    currentDate);
            contactDao.save(senderContact);
        } else {
            senderContact.setOtherUid(receiverUid);
            senderContact.setCreateTime(currentDate);
            senderContact.setMid(mid);
            senderContact.setType(msgType);
            contactDao.update(senderContact);
        }

        //存收件人最近聊天奶
        MessageContact receiverContact = contactDao.find(receiverUid);
        if (receiverContact == null) {
            receiverContact = new MessageContact(receiverUid, senderUid, mid, msgType,
                    currentDate);
        } else {
            receiverContact.setOtherUid(senderUid);
            receiverContact.setMid(mid);
            receiverContact.setType(msgType);
            receiverContact.setCreateTime(currentDate);
            contactDao.update(receiverContact);
        }

        MessageVO messageVO;
        //更新未读
        try (Jedis jedis = redisPool.getResource()) {
            jedis.decr(receiverUid + "_T"); //总未读
            jedis.decr(receiverUid + "_C");  //分未读


            //推送到redis
            User sender = userDao.get(senderUid);
            User receiver = userDao.get(receiverUid);
            messageVO = new MessageVO(mid, content, senderUid, msgType,
                    receiverUid, currentDate, sender.getAvator(), receiver.getAvator(),
                    sender.getUserName(), receiver.getUserName());
            jedis.publish(Constants.WEBSOCKET_MSG_TOPIC, JSONObject.toJSONString(messageVO));
        }
        return messageVO;
    }
}
