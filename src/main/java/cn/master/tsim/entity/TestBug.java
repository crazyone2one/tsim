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
 * 问题单(bug)
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("test_bug")
@Builder
@AllArgsConstructor
public class TestBug implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 项目id
     */
    @TableField("project_id")
    private String projectId;

    /**
     * suite id
     */
    @TableField("module_id")
    private String moduleId;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 严重程度(1:轻微,2:一般,3:严重,4:致命)
     */
    @TableField("severity")
    private String severity;

    /**
     * 功能点
     */
    @TableField("function")
    private String function;

    /**
     * 状态
     */
    @TableField("bug_status")
    private String bugStatus;

    /**
     * 备注内容
     */
    @TableField("note")
    private String note;

    /**
     * 测试人员
     */
    @TableField("tester")
    private String tester;

    /**
     * 测试计划id
     */
    @TableField("plan_id")
    private String planId;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private Date createDate;

    /**
     * 修改时间
     */
    @TableField("update_date")
    private Date updateDate;


}
