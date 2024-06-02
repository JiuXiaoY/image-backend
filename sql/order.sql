use image_api;

drop table order_info;

create table if not exists image_api.`order_info`
(
    `id`              bigint       not null auto_increment comment '主键' primary key,
    `serialNumber`    varchar(256) not null unique comment '流水号',
    `userId`          bigint       not null comment '用户id',
    `interfaceInfoId` bigint       not null comment '接口信息id',
    `amountPaid`      bigint       not null comment '支付金额',
    `paymentMethod`   varchar(256) null comment '支付方式',
    `purchasesCount`  bigint       null comment '购买次数',
    `status`          int          not null default 0 comment '订单状态（0 - 未完成，1 - 已完成）',
    `create_time`     datetime     not null default CURRENT_TIMESTAMP comment '创建时间',
    `update_time`     datetime     not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '更新时间',
    `isDeleted`       tinyint      not null default 0 comment '删除标志，默认0不删除，1删除'
) COMMENT '订单信息';