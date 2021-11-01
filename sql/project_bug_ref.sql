/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50732
Source Host           : 127.0.0.1:3306
Source Database       : tsim

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2021-11-01 15:28:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for project_bug_ref
-- ----------------------------
DROP TABLE IF EXISTS `project_bug_ref`;
CREATE TABLE `project_bug_ref` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `project_id` varchar(36) NOT NULL DEFAULT '' COMMENT 'projectId',
  `bug_id` varchar(36) NOT NULL DEFAULT '' COMMENT 'bugId',
  `work_date` varchar(50) NOT NULL DEFAULT '' COMMENT 'workDate',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='project_bug_ref';
