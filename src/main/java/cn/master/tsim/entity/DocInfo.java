package cn.master.tsim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 文档信息表
 * </p>
 *
 * @author 11's papa
 * @since 2021-11-19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("t_doc_info")
public class DocInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 文件名称
     */
    @TableField("doc_name")
    private String docName;
    @TableField("uuid_name")
    private String uuidName;

    /**
     * 文件类型
     */
    @TableField("doc_flag")
    private String docFlag;

    /**
     * 文件路径地址
     */
    @TableField("doc_path")
    private String docPath;

    /**
     * 删除标记 0-未删除 1-已删除
     */
    @TableField("del_flag")
    private Integer delFlag;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private Date createDate;

    /**
     * 更新时间
     */
    @TableField("update_date")
    private Date updateDate;


}
