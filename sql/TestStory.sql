-- auto Generated on 2021-10-18
-- DROP TABLE IF EXISTS test_story;
CREATE TABLE test_story(
	id VARCHAR (50) NOT NULL COMMENT '主键id',
	description VARCHAR (50) NOT NULL DEFAULT '' COMMENT '需求内容',
	project_id VARCHAR (50) NOT NULL DEFAULT '' COMMENT '项目id',
	work_date VARCHAR (50) NOT NULL DEFAULT '' COMMENT '需求时间',
	del_flag VARCHAR (50) NOT NULL DEFAULT '' COMMENT '完成状态(0:未结束，1：已结束)',
	create_date DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'createDate',
	update_date DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'updateDate',
	PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '需求表';
