package message.controller;

import com.alibaba.fastjson.JSONObject;
import message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("msg")
public class MessageController {

    @Autowired
    MessageService messageService;

    @RequestMapping("/sendMsg")
    public String sendMag(@RequestBody JSONObject jsonObject) {
        Long senderUid = jsonObject.getLong("senderUid");
        Long receiverUid = jsonObject.getLong("receiverUid");
        String content = jsonObject.getString("content");
        Integer msgType = jsonObject.getInteger("msg_type");
        messageService.sendMsg();
    }
}
