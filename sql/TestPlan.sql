-- auto Generated on 2021-10-18
-- DROP TABLE IF EXISTS test_plan;
CREATE TABLE test_plan(
	id VARCHAR (50) NOT NULL COMMENT '主键id',
	`name` VARCHAR (50) NOT NULL DEFAULT '' COMMENT '测试计划名称',
	description VARCHAR (50) NOT NULL DEFAULT '' COMMENT '测试计划描述',
	project_id VARCHAR (50) NOT NULL DEFAULT '' COMMENT '项目id',
	story_id VARCHAR (50) NOT NULL DEFAULT '' COMMENT '需求id',
	del_flag VARCHAR (50) NOT NULL DEFAULT '0' COMMENT '删除状态（0，有效 1，无效）',
	group_id VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'groupId',
	num VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'num',
	create_date DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'createDate',
	update_date DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'updateDate',
	PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '测试计划';