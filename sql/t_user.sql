/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50732
Source Host           : 127.0.0.1:3306
Source Database       : tsim

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2021-11-01 15:25:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tester
-- ----------------------------
DROP TABLE IF EXISTS `tester`;
CREATE TABLE `tester` (
  `id` varchar(255) NOT NULL COMMENT '主键id',
  `account` varchar(255) DEFAULT NULL COMMENT '账号',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `del_flag` varchar(255) DEFAULT '0' COMMENT '删除标记（0，未删除，1 删除）',
  `username` varchar(255) DEFAULT NULL COMMENT '真实姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
