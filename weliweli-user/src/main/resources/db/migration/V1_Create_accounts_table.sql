create table accounts
(
    uid          bigint auto_increment comment 'ID'     primary key,
    remarks     varchar(255) default ''                not null comment '备注',
    user_name   varchar(100)  default ''                not null comment '用户名',
    password    varchar(100) default ''                not null comment '密码',
    email       varchar(100) default ''                not null comment '邮箱',
    tel         varchar(100) default ''                not null comment '电话',
    avator     varchar(100) default ''                not null comment '头像地址',
    create_date timestamp    default CURRENT_TIMESTAMP not null,
    create_by   bigint       default -1                not null comment '创建人',
    update_date timestamp    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    update_by   bigint       default -1                not null,
    del_flag    tinyint      default 0                 not null comment '删除标记'
)
    comment '用户';