/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50732
Source Host           : 127.0.0.1:3306
Source Database       : tsim

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2021-11-01 16:01:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_project
-- ----------------------------
DROP TABLE IF EXISTS `t_project`;
CREATE TABLE `t_project` (
  `id` varchar(36) NOT NULL COMMENT '主键id',
  `project_name` varchar(50) NOT NULL DEFAULT '' COMMENT '项目名称',
  `project_code` varchar(50) NOT NULL DEFAULT '' COMMENT '项目code',
  `work_date` varchar(50) DEFAULT '' COMMENT 'workDate',
  `del_flag` tinyint(2) NOT NULL DEFAULT '0' COMMENT '删除标志(0=未删除;1=删除)',
  `create_data` datetime DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目表';
