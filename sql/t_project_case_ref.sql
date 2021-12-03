/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50732
Source Host           : 127.0.0.1:3306
Source Database       : tsim

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2021-12-03 14:14:35
*/

SET FOREIGN_KEY_CHECKS=0;

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
