CREATE TABLE `operation_logs` (
  `id`            INT(11)     NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `target_id`     INT(11)     NOT NULL COMMENT '对象id',
  `target_type`   INT(11)     NOT NULL COMMENT '对象类型',
  `operation_info` TEXT       NOT NULL COMMENT '操作记录',
  `operator_id`   INT(11)     NOT NULL COMMENT '操作人',
  `operator_name` VARCHAR(200) NOT NULL COMMENT '操作名称',
  `created_at`    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `ix_target_id_and_target_type` (`target_id`, `target_type`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = '操作记录';
