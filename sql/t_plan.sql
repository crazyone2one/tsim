/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50732
Source Host           : 127.0.0.1:3306
Source Database       : tsim

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2021-11-01 16:00:52
*/

SET FOREIGN_KEY_CHECKS=0;

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
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态（0，有效 1，无效）',
  `group_id` varchar(50) NOT NULL DEFAULT '' COMMENT 'groupId',
  `num` varchar(50) NOT NULL DEFAULT '' COMMENT 'num',
  `create_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'createDate',
  `update_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'updateDate',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试计划';
