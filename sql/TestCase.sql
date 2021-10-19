-- auto Generated on 2021-10-18
-- DROP TABLE IF EXISTS test_case;
CREATE TABLE test_case(
	id VARCHAR (50) NOT NULL COMMENT 'id',
	`name` VARCHAR (50) NOT NULL DEFAULT '' COMMENT '测试用例标题',
	description VARCHAR (50) NOT NULL DEFAULT '' COMMENT '测试描述',
	active VARCHAR (50) NOT NULL DEFAULT '0' COMMENT '删除状态',
	project_id VARCHAR (50) NOT NULL DEFAULT '' COMMENT '项目id',
	module_id VARCHAR (50) NOT NULL DEFAULT '' COMMENT '模块id',
	note VARCHAR (50) NOT NULL DEFAULT '' COMMENT '备注内容',
	precondition VARCHAR (50) NOT NULL DEFAULT '' COMMENT '测试前提',
	step_store VARCHAR (50) NOT NULL DEFAULT '' COMMENT '测试步骤',
	result_store VARCHAR (50) NOT NULL DEFAULT '' COMMENT '预期结果',
	priority VARCHAR (50) NOT NULL DEFAULT '' COMMENT '优先级(0=低;1=中;2=高)',
	test_mode VARCHAR (50) NOT NULL DEFAULT '' COMMENT '测试方式(0=手动;1=自动)',
    create_date DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'createDate',
    update_date DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'updateDate',
	PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '测试用例表';
