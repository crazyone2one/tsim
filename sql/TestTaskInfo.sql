-- auto Generated on 2021-10-18
-- DROP TABLE IF EXISTS test_task_info;
CREATE TABLE test_task_info(
	id VARCHAR (50) NOT NULL COMMENT '主键id',
	project_id VARCHAR (50) NOT NULL DEFAULT '' COMMENT '项目名称',
	summary_desc VARCHAR (50) NOT NULL DEFAULT '' COMMENT '任务描述',
	create_case_count INT (11) NOT NULL DEFAULT 0 COMMENT '编写用例数量',
	execute_case_count INT (11) NOT NULL DEFAULT 0 COMMENT '执行测试用例数量',
	finish_status VARCHAR (50) NOT NULL DEFAULT '' COMMENT '完成状态',
	delivery_status VARCHAR (50) NOT NULL DEFAULT '' COMMENT '交付状态',
    bug_doc tinyint(3),
    report_doc tinyint(3),
    req_doc tinyint(3),
	tester VARCHAR (50) NOT NULL DEFAULT '' COMMENT '负责人',
	remark VARCHAR (50) NOT NULL DEFAULT '' COMMENT '备注',
	issue_date VARCHAR (50) NOT NULL DEFAULT '' COMMENT '任务时间',
	create_date DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '创建时间',
	update_date DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '修改时间',
	PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '任务汇总信息表';
