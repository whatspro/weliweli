package admin.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {

    private String userName;

    private String email;

    private String tel;

    private String password;

    private String avator;

    private Date createTime;

}
