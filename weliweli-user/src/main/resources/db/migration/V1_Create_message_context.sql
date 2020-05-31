create table weliweli.message_context
(
    context     text      null,
    mid         bigint auto_increment
        primary key,
    type        char(16)  null,
    create_time timestamp null
);