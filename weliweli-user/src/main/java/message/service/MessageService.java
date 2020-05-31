package message.service;

import message.dao.MessageContentDao;
import message.dao.MessageRelationDao;
import message.entity.MessageContent;
import message.entity.MessageRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessageService {

    @Autowired
    MessageContentDao contentDao;

    @Autowired
    MessageRelationDao relationDao;

    public String sendMsg(Long senderUid, Long receiverUid, String content, Integer msgType) {
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

        //存最近聊天


        //更新未读

        //推送到redis

    }
}
