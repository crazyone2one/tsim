/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50732
Source Host           : 127.0.0.1:3306
Source Database       : tsim

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2021-11-01 16:01:02
*/

SET FOREIGN_KEY_CHECKS=0;

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
