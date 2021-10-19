-- auto Generated on 2021-10-18
-- DROP TABLE IF EXISTS `module`;
CREATE TABLE `module`(
	id VARCHAR (36) NOT NULL COMMENT '主键id',
	module_name VARCHAR (50) NOT NULL DEFAULT '' COMMENT '模块名称',
	module_code VARCHAR (50) NOT NULL DEFAULT '' COMMENT '模块code',
	project_id VARCHAR (50) NOT NULL DEFAULT '' COMMENT '项目id',
    del_flag VARCHAR (50) NOT NULL DEFAULT '0' COMMENT '删除标志(0=未删除;1=删除)',
	create_date DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'createDate',
	update_date DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '修改时间',
	PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '模块表';
