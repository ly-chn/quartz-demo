-- auto-generated definition
create table quartz_job
(
    id              int                  not null comment 'id'
        primary key,
    job_class_name  varchar(64)          not null comment '任务类名',
    cron_expression varchar(32)          not null comment 'cron表达式',
    parameter       varchar(256)         not null comment '参数名',
    started         tinyint(1) default 0 not null comment '启动状态'
);

