/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50732
Source Host           : 127.0.0.1:3306
Source Database       : tsim

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2021-12-03 14:14:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_task_info
-- ----------------------------
DROP TABLE IF EXISTS `t_task_info`;
CREATE TABLE `t_task_info` (
  `id` varchar(36) NOT NULL COMMENT '主键id',
  `project_id` varchar(36) NOT NULL DEFAULT '' COMMENT '项目名称',
  `story_id` varchar(36) NOT NULL COMMENT '需求id',
  `summary_desc` varchar(255) DEFAULT '' COMMENT '任务描述',
  `create_case_count` int(11) NOT NULL DEFAULT '0' COMMENT '编写用例数量',
  `execute_case_count` int(11) NOT NULL DEFAULT '0' COMMENT '执行测试用例数量',
  `finish_status` varchar(50) NOT NULL COMMENT '完成状态',
  `delivery_status` varchar(50) NOT NULL DEFAULT '' COMMENT '交付状态',
  `bug_doc` tinyint(1) DEFAULT NULL,
  `report_doc` tinyint(1) DEFAULT NULL,
  `req_doc` varchar(36) DEFAULT NULL COMMENT '需求文件数据id',
  `tester` varchar(36) NOT NULL DEFAULT '' COMMENT '负责人',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `issue_date` varchar(50) NOT NULL DEFAULT '' COMMENT '任务时间',
  `create_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务汇总信息表';
