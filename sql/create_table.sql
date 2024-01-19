-- auto-generated definition
create table user
(
    id         int auto_increment comment 'id'
        primary key,
    username   varchar(256)                       null comment '昵称',
    account    varchar(256)                       not null comment '账号',
    avatarUrl  varchar(1024)                      null comment '头像',
    gender     tinyint                            null comment '性别',
    password   varchar(512)                       not null comment '密码',
    phone      varchar(128)                       null comment '电话',
    email      varchar(128)                       null comment '邮箱',
    status     int      default 0                 not null comment '用户状态：0zhen',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDeleted  tinyint  default 0                 not null comment '是否被删除',
    userRole   int      default 0                 not null comment '用户身份：0-普通用户，1-管理员',
    planetCode varchar(512)                       null comment '星球编号'
)
    comment '用户';

