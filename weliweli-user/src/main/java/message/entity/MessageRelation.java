package message.entity;

import lombok.Data;

import java.util.Date;

@Data
public class MessageRelation {

    private Long mid;

    private Long ownerUid;

    private Integer type;

    private Long otherUid;

    private Date createTime;

    public MessageRelation(Long mid, Long ownerUid, Long otherUid, Integer type, Date createTime) {
        this.createTime = createTime;
        this.mid = mid;
        this.otherUid = otherUid;
        this.ownerUid = ownerUid;
        this.type = type;
    }
}
