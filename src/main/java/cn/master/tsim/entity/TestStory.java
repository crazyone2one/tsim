package cn.master.tsim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 需求表
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("test_story")
@Builder
@AllArgsConstructor
public class TestStory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 需求内容
     */
    @TableField("description")
    private String description;

    /**
     * 项目id
     */
    @TableField("project_id")
    private String projectId;

    /**
     * 需求时间
     */
    @TableField("data")
    private Date data;

    /**
     * 完成状态(0:未结束，1：已结束)
     */
    @TableField("del_flag")
    private String delFlag;

    @TableField("create_date")
    private Date createDate;

    @TableField("update_date")
    private Date updateDate;


}