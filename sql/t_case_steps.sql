/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50732
Source Host           : 127.0.0.1:3306
Source Database       : tsim

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2021-12-03 14:13:36
*/

SET FOREIGN_KEY_CHECKS=0;

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
