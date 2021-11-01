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
@NoArgsConstructor
public class TestBug implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 项目id
     */
    @TableField("project_id")
    private String projectId;
    @TableField(exist = false)
    private Project project;

    /**
     * suite id
     */
    @TableField("module_id")
    private String moduleId;
    @TableField(exist = false)
    private Module module;

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
    @TableField("func")
    private String func;

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
    @TableField(exist = false)
    private Tester testerEntity;

    @TableField("work_date")
    private String workDate;
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
