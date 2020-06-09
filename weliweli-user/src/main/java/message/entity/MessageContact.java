package message.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class MessageContact {
    private long ownerUid;

    private long otherUid;

    private long mid;

    private int type;

    private Date createTime;


}
