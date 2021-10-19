-- auto Generated on 2021-10-18
-- DROP TABLE IF EXISTS project_bug_ref;
CREATE TABLE project_bug_ref(
	id VARCHAR (36) NOT NULL COMMENT 'id',
	project_id VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'projectId',
	bug_id VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'bugId',
	work_date VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'workDate',
	PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'project_bug_ref';
