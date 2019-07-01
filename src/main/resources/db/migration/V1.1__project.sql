CREATE TABLE `projects` (
  `id`                      INT(11)         NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `sid`                     VARCHAR(100) COMMENT '项目id',
  `contract_subject`        VARCHAR(200) COMMENT '合同签署主体',
  `name`                    VARCHAR(200) NOT NULL COMMENT '项目名称',
  `film_start_at`           DATETIME     COMMENT '创建时间',
  `film_complete_duration`  INT(6) COMMENT '拍摄完成时长',
  `project_leader_id`       INT(6) COMMENT '项目负责人',
  `customer_manager_id`     INT(6) COMMENT '客户对接人',
  `director_id`             INT(6) COMMENT '导演',
  `executive_director_id`   INT(6) COMMENT '执行导演',
  `copywriting_id`          INT(6) COMMENT '文案',
  `producer_id`             INT(6) COMMENT '制片',
  `post_editing_id`         INT(6) COMMENT '后期剪辑',
  `compositing_id`          INT(6) COMMENT '后期合成',
  `art_id`                  INT(6) COMMENT '美术',
  `music_id`                INT(6) COMMENT '音乐',
  `storyboard_id`           INT(6) COMMENT '分镜',
  `contract_amount`   DECIMAL(15, 2) COMMENT '项目合同金额',
  `return_amount`     DECIMAL(15, 2) COMMENT '项目回款金额',
  `real_cost`         DECIMAL(15, 2) COMMENT '项目实际总成本',
  `budget_cost`       DECIMAL(15, 2) COMMENT '项目预算总成本',
  `shooting_budget`   DECIMAL(15, 2) COMMENT '拍摄预算',
  `late_state_budget` DECIMAL(15, 2) COMMENT '项目预算总成本',
  `shooting_cost`     DECIMAL(15, 2) COMMENT '项目预算总成本',
  `late_state_cost`   DECIMAL(15, 2) COMMENT '项目预算总成本',
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
  `stage`             INT(3) NOT NULL COMMENT '费用阶段',
  `category`          INT(5) NOT NULL COMMENT '费用大类',
  `child_category`    INT(5) NOT NULL COMMENT '费用子类',
  `desc`              VARCHAR(500)   NOT NULL COMMENT '定义',
  `budget_amount`     DECIMAL(15, 2) NOT NULL COMMENT '预算金额',
  `real_amount`       DECIMAL(15, 2) NOT NULL COMMENT '实际金额',
  `provider_id`       DECIMAL(15, 2) NOT NULL COMMENT '供应商id',
  `rank_score`        INT(5)      NOT NULL COMMENT '评分',
  `created_at`        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `ix_budget_amount` (`budget_amount`),
  KEY `ix_real_amount` (`real_amount`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = '项目费用表';

CREATE TABLE `staffs` (
  `id`                INT(11)     NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name`              VARCHAR(50) NOT NULL  COMMENT '用户名',
  `email`             VARCHAR(100)          COMMENT '邮箱',
  `cellphone`         VARCHAR(100)          COMMENT '电话',
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

