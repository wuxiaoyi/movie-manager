CREATE TABLE `projects` (
  `id`                      INT(11)       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `sid`                     VARCHAR(100)  COMMENT '项目id',
  `contract_subject_id`     INT(11)       COMMENT '合同签署主体',
  `name`                    VARCHAR(200)  NOT NULL COMMENT '项目名称',
  `film_duration`           INT(11)       COMMENT '成片时长',
  `shooting_start_at`       DATETIME      COMMENT '拍摄日期',
  `shooting_duration`       INT(6)  COMMENT '拍摄周期',
--   `project_leader_id`       INT(11) COMMENT '项目负责人',
--   `customer_manager_id`     INT(11) COMMENT '客户对接人',
--   `director_id`             INT(11) COMMENT '导演',
--   `executive_director_id`   INT(11) COMMENT '执行导演',
--   `copywriting_id`          INT(11) COMMENT '文案',
--   `producer_id`             INT(11) COMMENT '制片',
--   `post_editing_id`         INT(11) COMMENT '后期剪辑',
--   `compositing_id`          INT(11) COMMENT '后期合成',
--   `art_id`                  INT(11) COMMENT '美术',
--   `music_id`                INT(11) COMMENT '音乐',
--   `storyboard_id`           INT(11) COMMENT '分镜',
  `contract_amount`   DECIMAL(15, 2) COMMENT '项目合同金额',
  `return_amount`     DECIMAL(15, 2) COMMENT '项目回款金额',
  `real_cost`         DECIMAL(15, 2) COMMENT '项目实际总成本',
  `budget_cost`       DECIMAL(15, 2) COMMENT '项目预算总成本',
  `shooting_budget`   DECIMAL(15, 2) COMMENT '项目拍摄预算',
  `late_state_budget` DECIMAL(15, 2) COMMENT '项目后期预算',
  `shooting_cost`     DECIMAL(15, 2) COMMENT '项目拍摄成本',
  `late_state_cost`   DECIMAL(15, 2) COMMENT '项目后期成本',
  `state`             INT(3)          DEFAULT 0 COMMENT '项目状态',
  `created_at`        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `ix_name` (`name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = '项目表';

CREATE TABLE `project_members` (
  `id`                INT(11)     NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `project_id`        INT(11)     NOT NULL COMMENT '项目id',
  `member_type`       INT(3)      NOT NULL COMMENT '组员类型',
  `staff_id`          INT(11)     NOT NULL COMMENT '员工id',
  `created_at`        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `ix_staff_id` (`staff_id`),
  KEY `ix_project_id` (`project_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = '项目成员表';

CREATE TABLE `project_detail` (
  `id`                INT(11)     NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `project_id`        INT(11)     NOT NULL COMMENT '项目id',
  `stage`             INT(3)      NOT NULL COMMENT '费用阶段',
  `fee_category_id`          INT(8) NOT NULL COMMENT '费用大类',
  `fee_child_category_id`    INT(8) COMMENT '费用子类',
  `desc`              VARCHAR(500)         COMMENT '定义',
  `budget_amount`     DECIMAL(15, 2) default 0 COMMENT '预算金额',
  `real_amount`       DECIMAL(15, 2) default 0 COMMENT '实际金额',
  `provider_id`       INT(8)               COMMENT '供应商id',
  `rank_score`        INT(5)               COMMENT '评分',
  `remark`            TEXT                 COMMENT '备注',
  `created_at`        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `ix_fee_category_id` (`fee_category_id`),
  KEY `ix_fee_child_category_id` (`fee_child_category_id`),
  KEY `ix_budget_amount` (`budget_amount`),
  KEY `ix_real_amount` (`real_amount`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = '项目费用表';

CREATE TABLE `staffs` (
  `id`                INT(11)     NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name`              VARCHAR(50) NOT NULL  COMMENT '用户名',
  `email`             VARCHAR(100)          COMMENT '邮箱',
  `cellphone`         VARCHAR(100)          COMMENT '电话',
  `ascription`        INT(3)      DEFAULT 1 COMMENT '员工归属，1内部员工，2外部员工',
  `state`             INT(3)      DEFAULT 0 COMMENT '状态，0正常，1禁用',
  `created_at`        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `ix_name` (`name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = '公司员工表';

CREATE TABLE `providers` (
  `id`                INT(11)     NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `name`              VARCHAR(50) NOT NULL  COMMENT '供应商名',
  `bank_account`      VARCHAR(100)          COMMENT '银行账户',
  `bank_name`         VARCHAR(100)          COMMENT '开户行',
  `cellphone`         VARCHAR(100)          COMMENT '联系方式',
  `state`             INT(3)      DEFAULT 0 COMMENT '状态，0正常，1禁用',
  `created_at`        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `ix_name` (`name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = '供应商表';

CREATE TABLE `contract_subjects` (
  `id`                INT(11)     NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `name`              VARCHAR(50) NOT NULL  COMMENT '主体名',
  `state`             INT(3)      DEFAULT 0 COMMENT '状态，0正常，1禁用',
  `created_at`        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = '合同签署主体表';

CREATE TABLE `fee_category` (
  `id`                  INT(11)     NOT NULL  AUTO_INCREMENT COMMENT '自增主键',
  `name`                VARCHAR(50) NOT NULL  COMMENT '费用名称',
  `category_type`       INT(3)      NOT NULL  COMMENT '费用级别，1：一级，2：二级',
  `parent_category_id`  INT(11)     COMMENT '父config id',
  `stage`               INT(3)      DEFAULT 0 COMMENT '费用阶段',
  `state`               INT(3)      DEFAULT 0 COMMENT '状态，0正常，1禁用',
  `created_at`          DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`          DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = '费用配置表';