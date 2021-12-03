/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50732
Source Host           : 127.0.0.1:3306
Source Database       : tsim

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2021-12-03 14:13:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_module
-- ----------------------------
DROP TABLE IF EXISTS `t_module`;
CREATE TABLE `t_module` (
  `id` varchar(36) NOT NULL COMMENT '主键id',
  `module_name` varchar(50) NOT NULL DEFAULT '' COMMENT '模块名称',
  `module_code` varchar(50) NOT NULL DEFAULT '' COMMENT '模块code',
  `project_id` varchar(50) NOT NULL DEFAULT '' COMMENT '项目id',
  `del_flag` tinyint(2) NOT NULL DEFAULT '0' COMMENT '删除标志(0=未删除;1=删除)',
  `create_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'createDate',
  `update_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模块表';
