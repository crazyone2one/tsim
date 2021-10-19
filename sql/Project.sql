-- auto Generated on 2021-10-18
-- DROP TABLE IF EXISTS project;
CREATE TABLE project(
	id VARCHAR (32) NOT NULL COMMENT '主键id',
	project_name VARCHAR (50) NOT NULL DEFAULT '' COMMENT '项目名称',
	project_code VARCHAR (50) NOT NULL DEFAULT '' COMMENT '项目code',
	work_date VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'workDate',
    del_flag VARCHAR (50) NOT NULL DEFAULT '0' COMMENT '删除标志(0=未删除;1=删除)',
	create_data DATETIME DEFAULT null COMMENT '创建时间',
	update_date DATETIME DEFAULT null COMMENT '修改时间',
	PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '项目表';
