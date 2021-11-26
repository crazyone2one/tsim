-- tsim.t_doc_info definition

CREATE TABLE `t_doc_info` (
                              `id` varchar(36) NOT NULL COMMENT '主键id',
                              `doc_name` varchar(100) NOT NULL COMMENT '文件名称',
                              `uuid_name` varchar(100) NOT NULL COMMENT 'uuid生成的文件名',
                              `doc_flag` varchar(100) NOT NULL COMMENT '文件类型',
                              `doc_path` varchar(100) NOT NULL COMMENT '文件路径地址',
                              `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标记 0-未删除 1-已删除',
                              `create_date` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_date` datetime DEFAULT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文档信息表';