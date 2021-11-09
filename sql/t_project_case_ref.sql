/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : 127.0.0.1:3306
 Source Schema         : tsim

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 09/11/2021 09:03:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_project_case_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_project_case_ref`;
CREATE TABLE `t_project_case_ref`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `project_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'projectId',
  `case_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'caseId',
  `work_date` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'workDate',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目-测试用例关联表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
