use image_api;

drop table image_api.`interface_info`;

create table if not exists image_api.`interface_info`
(
    `id`             bigint       not null auto_increment comment '主键' primary key,
    `name`           varchar(256) not null comment '名称',
    `description`    varchar(256) null comment '描述',
    `url`            varchar(512) not null comment '接口地址',
    `requestParams`  text         not null comment '请求参数',
    `requestHeader`  text         null comment '请求头',
    `responseHeader` text         null comment '响应头',
    `status`         int          not null default 0 comment '接口状态（0 - 关闭，1 - 开启）',
    `method`         varchar(256) not null comment '请求类型',
    `userid`         bigint       not null comment '创建人',
    `create_time`    datetime     not null default CURRENT_TIMESTAMP comment '创建时间',
    `update_time`    datetime     not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '更新时间',
    `isDeleted`      tinyint      not null default 0 comment '删除标志，默认0不删除，1删除'
) COMMENT '接口信息';

INSERT INTO image_api.interface_info
(`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `userid`)
VALUES ('接口1', '这是接口1的描述', 'https://example.com/api1', 'username:text1',
        '{"Content-Type": "application/json"}',
        '{"Content-Type": "application/json"}', 1, 'GET', 10),
       ('接口2', '这是接口2的描述', 'https://example.com/api2', 'username:text1', '{"Authorization": "Bearer token"}',
        '{"Content-Type": "application/json"}', 0, 'POST', 11),
       ('接口3', '这是接口3的描述', 'https://example.com/api3', 'username:text1', '{"Content-Type": "application/xml"}',
        '{"Content-Type": "application/xml"}', 1, 'PUT', 12),
       ('接口4', '这是接口4的描述', 'https://example.com/api4', 'username:text1',
        '{"Authorization": "Basic base64encoded"}',
        '{"Content-Type": "application/json"}', 1, 'GET', 13),
       ('接口5', '这是接口5的描述', 'https://example.com/api5', 'username:text1',
        '{"Content-Type": "application/json"}',
        '{"Content-Type": "application/json"}', 0, 'POST', 14),
       ('接口6', '这是接口6的描述', 'https://example.com/api6', 'username:text1', '{"Authorization": "Bearer token"}',
        '{"Content-Type": "application/xml"}', 1, 'GET', 15),
       ('接口7', '这是接口7的描述', 'https://example.com/api7', 'username:text1',
        '{"Content-Type": "application/json"}',
        '{"Content-Type": "application/json"}', 1, 'PUT', 16),
       ('接口8', '这是接口8的描述', 'https://example.com/api8', 'username:text1',
        '{"Authorization": "Basic base64encoded"}',
        '{"Content-Type": "application/json"}', 0, 'POST', 17),
       ('接口9', '这是接口9的描述', 'https://example.com/api9', 'username:text1', '{"Content-Type": "application/xml"}',
        '{"Content-Type": "application/xml"}', 1, 'PUT', 18),
       ('接口10', '这是接口10的描述', 'https://example.com/api10', 'username:text1',
        '{"Authorization": "Bearer token"}',
        '{"Content-Type": "application/json"}', 1, 'GET', 19);


CREATE TABLE IF NOT EXISTS user_interface_info
(
    `id`              BIGINT                                                         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `userId`          BIGINT                                                         NOT NULL COMMENT '调用用户 id',
    `interfaceInfoId` BIGINT                                                         NOT NULL COMMENT '接口 id',
    `totalNum`        INT      DEFAULT 0                                             NOT NULL COMMENT '总调用次数',
    `leftNum`         INT      DEFAULT 0                                             NOT NULL COMMENT '剩余调用次数',
    `status`          INT      DEFAULT 0                                             NOT NULL COMMENT '0-正常，1-禁用',
    `create_time`     DATETIME DEFAULT CURRENT_TIMESTAMP                             NOT NULL COMMENT '创建时间',
    `update_time`     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '更新时间',
    `isDeleted`       TINYINT  DEFAULT 0                                             NOT NULL COMMENT '是否删除(0-未删，1-已删)',
    PRIMARY KEY (id)
) COMMENT '用户调用接口关系表';

