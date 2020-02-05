CREATE TABLE `project_permissions` (
  `id`                INT(11)     NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `project_id`        INT(11)     NOT NULL COMMENT '项目id',
  `permission_type`   INT(3)      NOT NULL COMMENT '权限类型，1：读，2：改',
  `operator_id`       INT(11)     NOT NULL COMMENT '操作人id',
  `user_id`           INT(11)     NOT NULL COMMENT '授权用户id',
  `created_at`        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `ix_project_id` (`project_id`),
  KEY `ix_user_id` (`user_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = '项目权限表';

ALTER TABLE `projects` add column `creator_id` INT(11) NOT NULL COMMENT '创建人id';