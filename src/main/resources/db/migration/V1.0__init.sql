CREATE TABLE `users` (
  `id`            INT(11)     NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name`          VARCHAR(50) NOT NULL COMMENT '用户名',
  `email`         VARCHAR(100) NOT NULL COMMENT '邮箱',
  `cellphone`     VARCHAR(100) NOT NULL COMMENT '电话',
  `password`      VARCHAR(50) NOT NULL COMMENT '密码',
  `password_slat` VARCHAR(50) NOT NULL COMMENT '密码盐',
  `created_at`    DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`    DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_email` (`email`),
  UNIQUE KEY `ix_cellphone` (`cellphone`),
  KEY `ix_name` (`name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = '用户';

CREATE TABLE `user_roles` (
  `id`            INT(11)       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id`       INT(11)       NOT NULL COMMENT '用户id',
  `role_id`       INT(11)       NOT NULL COMMENT '角色id',
  `created_at`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_user_id_and_role_id` (`user_id`, `role_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = '用户角色表';

CREATE TABLE `roles` (
  `id`            INT(11)       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name`          VARCHAR(100)  NOT NULL COMMENT '用户id',
  `created_at`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = '角色表';

CREATE TABLE `permissions` (
  `id`            INT(11)       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name`          VARCHAR(100)  NOT NULL COMMENT '权限名称',
  `desc`          VARCHAR(255)  NOT NULL COMMENT '权限描述',
  `created_at`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = '权限表';


CREATE TABLE `permission_roles` (
  `id`            INT(11)       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `permission_id` INT(11)       NOT NULL COMMENT '权限id',
  `role_id`       INT(11)       NOT NULL COMMENT '角色id',
  `created_at`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_permission_id_and_role_id` (`permission_id`, `role_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = '权限表';