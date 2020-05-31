create table weliweli.message_relation
(
    owner_uid   bigint    not null,
    other_uid   bigint    not null,
    mid         bigint    not null,
    type        int       not null,
    create_time timestamp not null,
    primary key (owner_uid, mid)
);

create index message_relation__idx_owneruid_otheruid_mid
    on weliweli.message_relation (owner_uid, other_uid, mid);