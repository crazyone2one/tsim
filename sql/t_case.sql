/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50732
Source Host           : 127.0.0.1:3306
Source Database       : tsim

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2021-12-03 14:13:26
*/

SET FOREIGN_KEY_CHECKS=0;

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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试用例表';
