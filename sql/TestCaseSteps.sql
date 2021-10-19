-- auto Generated on 2021-10-18
-- DROP TABLE IF EXISTS test_case_steps;
CREATE TABLE test_case_steps(
	id VARCHAR (50) NOT NULL COMMENT 'id',
	case_id VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'caseId',
	case_order INT (11) NOT NULL DEFAULT -1 COMMENT 'caseOrder',
	case_step VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'caseStep',
	case_step_result VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'caseStepResult',
	active VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'active',
	create_date DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'createDate',
	update_date DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'updateDate',
	PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'test_case_steps';
