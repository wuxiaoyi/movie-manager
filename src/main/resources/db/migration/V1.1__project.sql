CREATE TABLE `projects` (
  `id`                  INT(11)         NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `sid`                 VARCHAR(100)    NOT NULL AUTO_INCREMENT COMMENT '项目id',
  `name`                VARCHAR(200)    NOT NULL COMMENT '项目名称',
  `film_complete_duration` INT(5)   NOT NULL COMMENT '邮箱',
  `project_leader`      VARCHAR(100) COMMENT '项目负责人',
  `customer_manager`    VARCHAR(100) COMMENT '客户对接人',
  `director`            VARCHAR(100) COMMENT '导演',
  `executive_director`  VARCHAR(100) COMMENT '执行导演',
  `copywriting`         VARCHAR(100) COMMENT '文案',
  `producer`            VARCHAR(100) COMMENT '密码',
  `post_editing`        VARCHAR(100) COMMENT '后期剪辑',
  `compositing`         VARCHAR(100) COMMENT '后期合成',
  `art`                 VARCHAR(100) COMMENT '美术',
  `music`               VARCHAR(100) COMMENT '音乐',
  `storyboard`          VARCHAR(100) COMMENT '分镜',
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
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = '项目表';

CREATE TABLE `project_detail` (
  `id`                INT(11)     NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `project_id`        INT(11)     NOT NULL AUTO_INCREMENT COMMENT '项目id',
  `stage`             INT(3) NOT NULL COMMENT '费用阶段',
  `category`          INT(5) NOT NULL COMMENT '费用大类',
  `child_category`    INT(5) NOT NULL COMMENT '费用子类',
  `desc`              VARCHAR(500)   NOT NULL COMMENT '定义',
  `budget_amount`     DECIMAL(15, 2) NOT NULL COMMENT '预算金额',
  `real_amount`       DECIMAL(15, 2) NOT NULL COMMENT '实际金额',
  `created_at`        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = '项目表';

