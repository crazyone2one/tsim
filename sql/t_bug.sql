/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50732
Source Host           : 127.0.0.1:3306
Source Database       : tsim

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2021-12-03 14:13:18
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_bug
-- ----------------------------
DROP TABLE IF EXISTS `t_bug`;
CREATE TABLE `t_bug` (
  `id` varchar(36) NOT NULL COMMENT '主键id',
  `project_id` varchar(36) NOT NULL DEFAULT '' COMMENT '项目id',
  `module_id` varchar(36) NOT NULL DEFAULT '' COMMENT '模块 id',
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '标题',
  `severity` int(11) NOT NULL DEFAULT '1' COMMENT '严重程度(1:轻微,2:一般,3:严重,4:致命)',
  `func` varchar(100) NOT NULL DEFAULT '' COMMENT '功能点',
  `bug_status` int(11) NOT NULL DEFAULT '1' COMMENT '状态（1,新增 2，挂起 3，已提交 4，已解决 5 非bug）',
  `note` varchar(100) NOT NULL DEFAULT '' COMMENT '备注内容',
  `tester` varchar(36) NOT NULL DEFAULT '' COMMENT '测试人员',
  `work_date` varchar(50) NOT NULL DEFAULT '' COMMENT 'workDate',
  `plan_id` varchar(36) NOT NULL DEFAULT '' COMMENT '测试计划id',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志(0=未删除;1=删除)',
  `create_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问题单(bug)表';
