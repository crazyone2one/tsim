/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50732
Source Host           : 127.0.0.1:3306
Source Database       : tsim

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2022-01-14 16:32:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for project_bug_ref
-- ----------------------------
DROP TABLE IF EXISTS `project_bug_ref`;
CREATE TABLE `project_bug_ref` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `project_id` varchar(36) NOT NULL DEFAULT '' COMMENT 'projectId',
  `story_id` varchar(36) DEFAULT NULL COMMENT '需求id',
  `bug_id` varchar(36) NOT NULL DEFAULT '' COMMENT 'bugId',
  `work_date` varchar(50) NOT NULL DEFAULT '' COMMENT 'workDate',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='project_bug_ref';

-- ----------------------------
-- Table structure for t_bug
-- ----------------------------
DROP TABLE IF EXISTS `t_bug`;
CREATE TABLE `t_bug` (
  `id` varchar(36) NOT NULL COMMENT '主键id',
  `project_id` varchar(36) NOT NULL DEFAULT '' COMMENT '项目id',
  `module_id` varchar(36) NOT NULL DEFAULT '' COMMENT '模块 id',
  `func` varchar(100) NOT NULL DEFAULT '' COMMENT '功能点',
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '标题',
  `reproduce_steps` varchar(255) DEFAULT '' COMMENT '问题复现步骤',
  `expect_result` varchar(255) DEFAULT '' COMMENT '预期结果',
  `actual_result` varchar(255) DEFAULT '' COMMENT '实际结果',
  `bug_description` varchar(255) DEFAULT '' COMMENT '问题单内容描述',
  `bug_recurrence_probability` int(10) DEFAULT '1' COMMENT '复现概率',
  `severity` int(11) NOT NULL DEFAULT '1' COMMENT '严重程度(1:轻微,2:一般,3:严重,4:致命)',
  `bug_status` int(11) NOT NULL DEFAULT '1' COMMENT '状态（1,新增 2，挂起 3，已提交 4，已解决 5 非bug）',
  `note` varchar(100) NOT NULL DEFAULT '' COMMENT '备注内容',
  `tester` varchar(36) NOT NULL DEFAULT '' COMMENT '测试人员',
  `work_date` varchar(50) NOT NULL DEFAULT '' COMMENT 'workDate',
  `plan_id` varchar(36) NOT NULL DEFAULT '' COMMENT '测试计划id',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志(0=未删除;1=删除)',
  `create_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问题单(bug)表';

-- ----------------------------
-- Table structure for t_case
-- ----------------------------
DROP TABLE IF EXISTS `t_case`;
CREATE TABLE `t_case` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '测试用例标题',
  `description` varchar(100) DEFAULT '' COMMENT '测试描述',
  `active` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志(0=未删除;1=删除)',
  `project_id` varchar(36) NOT NULL DEFAULT '' COMMENT '项目id',
  `module_id` varchar(36) NOT NULL DEFAULT '' COMMENT '模块id',
  `note` varchar(100) DEFAULT '' COMMENT '备注内容',
  `precondition` varchar(255) DEFAULT '' COMMENT '测试前提',
  `step_store` varchar(255) NOT NULL DEFAULT '' COMMENT '测试步骤',
  `result_store` varchar(255) NOT NULL DEFAULT '' COMMENT '预期结果',
  `priority` tinyint(1) NOT NULL DEFAULT '1' COMMENT '优先级(0=低;1=中;2=高)',
  `test_mode` tinyint(1) NOT NULL DEFAULT '0' COMMENT '测试方式(0=手动;1=自动)',
  `create_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'createDate',
  `update_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'updateDate',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试用例表';

-- ----------------------------
-- Table structure for t_case_steps
-- ----------------------------
DROP TABLE IF EXISTS `t_case_steps`;
CREATE TABLE `t_case_steps` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `case_id` varchar(36) NOT NULL DEFAULT '' COMMENT 'caseId',
  `case_order` int(11) NOT NULL DEFAULT '-1' COMMENT 'caseOrder',
  `case_step` varchar(255) NOT NULL DEFAULT '' COMMENT 'caseStep',
  `case_step_result` varchar(255) NOT NULL DEFAULT '' COMMENT 'caseStepResult',
  `active` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'active',
  `create_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'createDate',
  `update_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'updateDate',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='test_case_steps';

-- ----------------------------
-- Table structure for t_doc_info
-- ----------------------------
DROP TABLE IF EXISTS `t_doc_info`;
CREATE TABLE `t_doc_info` (
  `id` varchar(36) NOT NULL COMMENT '主键id',
  `doc_name` varchar(100) NOT NULL COMMENT '文件名称',
  `uuid_name` varchar(100) NOT NULL COMMENT 'uuid生成的文件名',
  `doc_flag` varchar(100) NOT NULL COMMENT '文件类型',
  `doc_path` varchar(100) NOT NULL COMMENT '文件路径地址',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标记 0-未删除 1-已删除',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文档信息表';

-- ----------------------------
-- Table structure for t_module
-- ----------------------------
DROP TABLE IF EXISTS `t_module`;
CREATE TABLE `t_module` (
  `id` varchar(36) NOT NULL COMMENT '主键id',
  `module_name` varchar(50) NOT NULL DEFAULT '' COMMENT '模块名称',
  `module_code` varchar(50) NOT NULL DEFAULT '' COMMENT '模块code',
  `project_id` varchar(50) NOT NULL DEFAULT '' COMMENT '项目id',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志(0=未删除;1=删除)',
  `create_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'createDate',
  `update_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模块表';

-- ----------------------------
-- Table structure for t_plan
-- ----------------------------
DROP TABLE IF EXISTS `t_plan`;
CREATE TABLE `t_plan` (
  `id` varchar(36) NOT NULL COMMENT '主键id',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '测试计划名称',
  `description` varchar(255) NOT NULL DEFAULT '' COMMENT '测试计划描述',
  `project_id` varchar(36) NOT NULL DEFAULT '' COMMENT '项目id',
  `story_id` varchar(36) NOT NULL DEFAULT '' COMMENT '需求id',
  `work_date` varchar(50) DEFAULT '' COMMENT '任务时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态（0，有效 1，无效）',
  `group_id` varchar(50) NOT NULL DEFAULT '' COMMENT 'groupId',
  `num` varchar(50) NOT NULL DEFAULT '' COMMENT 'num',
  `create_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'createDate',
  `update_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'updateDate',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试计划';

-- ----------------------------
-- Table structure for t_plan_case_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_plan_case_ref`;
CREATE TABLE `t_plan_case_ref` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `plan_id` varchar(36) NOT NULL COMMENT '测试计划id',
  `case_id` varchar(36) NOT NULL DEFAULT '' COMMENT '测试用例id',
  `run_status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否执行（0，未执行，1，已执行）',
  `run_result` tinyint(2) DEFAULT NULL COMMENT '执行结果（0，通过，1，未通过）',
  `bug_id` varchar(36) DEFAULT NULL COMMENT 'bug id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_plan_story_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_plan_story_ref`;
CREATE TABLE `t_plan_story_ref` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `plan_id` varchar(36) DEFAULT '' COMMENT '测试计划数据id',
  `story_id` varchar(36) DEFAULT '' COMMENT '需求数据id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='测试计划与测试需求关联关系表';

-- ----------------------------
-- Table structure for t_project
-- ----------------------------
DROP TABLE IF EXISTS `t_project`;
CREATE TABLE `t_project` (
  `id` varchar(36) NOT NULL COMMENT '主键id',
  `project_name` varchar(50) NOT NULL DEFAULT '' COMMENT '项目名称',
  `project_code` varchar(50) NOT NULL DEFAULT '' COMMENT '项目code',
  `work_date` varchar(50) DEFAULT '' COMMENT 'workDate',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志(0=未删除;1=删除)',
  `create_data` datetime DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目表';

-- ----------------------------
-- Table structure for t_project_case_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_project_case_ref`;
CREATE TABLE `t_project_case_ref` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `project_id` varchar(36) NOT NULL DEFAULT '' COMMENT '项目Id',
  `story_id` varchar(36) NOT NULL COMMENT '需求id',
  `case_id` varchar(36) NOT NULL DEFAULT '' COMMENT '测试用例id',
  `work_date` varchar(10) DEFAULT '' COMMENT 'workDate',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目-测试用例关联表';

-- ----------------------------
-- Table structure for t_story
-- ----------------------------
DROP TABLE IF EXISTS `t_story`;
CREATE TABLE `t_story` (
  `id` varchar(36) NOT NULL COMMENT '主键id',
  `description` varchar(255) NOT NULL DEFAULT '' COMMENT '需求内容',
  `project_id` varchar(36) NOT NULL DEFAULT '' COMMENT '项目id',
  `work_date` varchar(50) NOT NULL DEFAULT '' COMMENT '需求时间',
  `story_status` tinyint(1) DEFAULT '0' COMMENT '完成状态(0-未完成,1-已完成）',
  `doc_id` varchar(36) DEFAULT NULL,
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态(0:未删除，1：已删除)',
  `create_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'createDate',
  `update_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'updateDate',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='需求表';

-- ----------------------------
-- Table structure for t_task_info
-- ----------------------------
DROP TABLE IF EXISTS `t_task_info`;
CREATE TABLE `t_task_info` (
  `id` varchar(36) NOT NULL COMMENT '主键id',
  `project_id` varchar(36) NOT NULL DEFAULT '' COMMENT '项目名称',
  `story_id` varchar(36) NOT NULL DEFAULT '' COMMENT '需求id',
  `plan_id` varchar(36) DEFAULT '' COMMENT '测试计划数据id',
  `summary_desc` varchar(255) DEFAULT '' COMMENT '任务描述',
  `create_case_count` int(11) NOT NULL DEFAULT '0' COMMENT '编写用例数量',
  `execute_case_count` int(11) NOT NULL DEFAULT '0' COMMENT '执行测试用例数量',
  `finish_status` varchar(50) NOT NULL COMMENT '完成状态（0-已完成，1-进行中，2-待回测，3-已回测）',
  `delivery_status` varchar(10) NOT NULL DEFAULT '' COMMENT '交付状态(0-是，1-否，2-不确定)',
  `bug_doc` tinyint(1) DEFAULT NULL,
  `report_doc` tinyint(1) DEFAULT NULL,
  `req_doc` varchar(36) DEFAULT NULL COMMENT '需求文件数据id',
  `tester` varchar(36) NOT NULL DEFAULT '' COMMENT '负责人',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除状态',
  `issue_date` varchar(50) NOT NULL DEFAULT '' COMMENT '任务时间',
  `create_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务汇总信息表';

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` varchar(255) NOT NULL COMMENT '主键id',
  `account` varchar(36) NOT NULL COMMENT '账号',
  `username` varchar(36) NOT NULL COMMENT '真实姓名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `email` varchar(36) DEFAULT NULL COMMENT '邮箱',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标记（0，未删除，1 删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
