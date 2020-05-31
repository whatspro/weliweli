package message.entity;

import lombok.Data;

import java.util.Date;

@Data
public class MessageContent {
    private Long mid;
    private Long senderId;
    private Long recipientId;
    private String content;
    private Integer msgType;
    private Date createTime;
}
