/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50732
Source Host           : 127.0.0.1:3306
Source Database       : tsim

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2021-09-27 17:25:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for build
-- ----------------------------
DROP TABLE IF EXISTS `build`;
CREATE TABLE `build` (
  `id` varchar(32) NOT NULL,
  `module_id` varchar(32) DEFAULT NULL,
  `version` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for group
-- ----------------------------
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `id` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `module_id` varchar(32) DEFAULT NULL,
  `priority` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for group_test_case
-- ----------------------------
DROP TABLE IF EXISTS `group_test_case`;
CREATE TABLE `group_test_case` (
  `id` varchar(32) NOT NULL,
  `group_id` varchar(32) DEFAULT NULL,
  `test_case_id` varchar(32) DEFAULT NULL,
  `num` int(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for module
-- ----------------------------
DROP TABLE IF EXISTS `module`;
CREATE TABLE `module` (
  `id` varchar(36) NOT NULL COMMENT '主键id',
  `module_name` varchar(255) DEFAULT NULL COMMENT '模块名称',
  `module_code` varchar(255) DEFAULT NULL COMMENT '模块code',
  `project_id` varchar(255) DEFAULT NULL COMMENT '项目id',
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(255) DEFAULT NULL COMMENT '删除标志(0=未删除;1=删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for node_types
-- ----------------------------
DROP TABLE IF EXISTS `node_types`;
CREATE TABLE `node_types` (
  `id` int(10) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `id` varchar(32) NOT NULL COMMENT '主键id',
  `project_name` varchar(255) DEFAULT NULL COMMENT '项目名称',
  `project_code` varchar(255) DEFAULT NULL COMMENT '项目code',
  `create_data` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(6) DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(255) DEFAULT NULL COMMENT '删除标记(0=未删除;1=删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='项目表';

-- ----------------------------
-- Table structure for test_case
-- ----------------------------
DROP TABLE IF EXISTS `test_case`;
CREATE TABLE `test_case` (
  `id` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '测试用例标题',
  `precondition` varchar(255) DEFAULT NULL COMMENT '测试前提',
  `description` varchar(255) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL COMMENT '备注内容',
  `active` varchar(10) DEFAULT NULL COMMENT '删除标志(0=未删除;1=删除)',
  `project_id` varchar(32) DEFAULT NULL COMMENT '项目',
  `module_id` varchar(32) DEFAULT NULL COMMENT '项目模块',
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `step_store` varchar(255) DEFAULT NULL COMMENT '详细步骤',
  `result_store` varchar(255) DEFAULT NULL COMMENT '期望结果',
  `priority` varchar(255) DEFAULT NULL COMMENT ' 优先级(0=低;1=中;2=高)',
  `test_mode` varchar(255) DEFAULT NULL COMMENT '测试方式(0=手动;1=自动)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for test_case_steps
-- ----------------------------
DROP TABLE IF EXISTS `test_case_steps`;
CREATE TABLE `test_case_steps` (
  `id` varchar(36) NOT NULL,
  `case_id` varchar(36) DEFAULT NULL,
  `case_order` int(10) DEFAULT NULL,
  `case_step` varchar(255) DEFAULT NULL,
  `case_step_result` varchar(255) DEFAULT NULL,
  `active` varchar(10) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for test_plan
-- ----------------------------
DROP TABLE IF EXISTS `test_plan`;
CREATE TABLE `test_plan` (
  `id` varchar(32) NOT NULL,
  `story_id` varchar(32) DEFAULT NULL,
  `group_id` varchar(32) DEFAULT NULL,
  `num` varchar(32) DEFAULT NULL,
  `type_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
