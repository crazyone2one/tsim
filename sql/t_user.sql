/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50732
Source Host           : 127.0.0.1:3306
Source Database       : tsim

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2021-12-03 14:15:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` varchar(255) NOT NULL COMMENT '主键id',
  `account` varchar(255) DEFAULT NULL COMMENT '账号',
  `username` varchar(255) DEFAULT NULL COMMENT '真实姓名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `del_flag` varchar(255) DEFAULT '0' COMMENT '删除标记（0，未删除，1 删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
