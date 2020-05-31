create table message_contact
(
    owner_uid bigint not null,
    other_uid bigint not null,
    mid bigint not null,
    type int not null,
    create_time timestamp not null,
    constraint message_contact_pk
        primary key (owner_uid, other_uid)
);


