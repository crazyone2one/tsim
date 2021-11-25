/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50732
Source Host           : 127.0.0.1:3306
Source Database       : tsim

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2021-11-01 16:01:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_story
-- ----------------------------
DROP TABLE IF EXISTS `t_story`;
CREATE TABLE `t_story` (
  `id` varchar(36) NOT NULL COMMENT '主键id',
  `description` varchar(255) NOT NULL DEFAULT '' COMMENT '需求内容',
  `project_id` varchar(36) NOT NULL DEFAULT '' COMMENT '项目id',
  `work_date` varchar(50) NOT NULL DEFAULT '' COMMENT '需求时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '完成状态(0:未结束，1：已结束)',
  `create_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'createDate',
  `update_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'updateDate',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='需求表';


ALTER TABLE t_story ADD doc_id varchar(36) NULL COMMENT '文件id';
ALTER TABLE t_story CHANGE doc_id doc_id varchar(36) NULL AFTER work_date;