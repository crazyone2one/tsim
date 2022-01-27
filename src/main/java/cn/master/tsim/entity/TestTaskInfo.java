package cn.master.tsim.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 任务汇总表
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_task_info")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestTaskInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名称", index = 0)
    @TableField("project_id")
    private String projectId;
    @TableField("story_id")
    private String storyId;
    @TableField(exist = false)
    private TestStory testStory;

    @TableField("plan_id")
    private String planId;
    /**
     * 任务描述
     */
    @ExcelProperty(value = "任务描述", index = 1)
    @TableField("summary_desc")
    private String summaryDesc;

    /**
     * 编写用例数量
     */
    @ExcelProperty({"用例数量", "编写用例"})
    @TableField("create_case_count")
    private Integer createCaseCount;

    /**
     * 执行测试用例数量
     */
    @ExcelProperty({"用例数量", "执行用例"})
    @TableField("execute_case_count")
    private Integer executeCaseCount;

    /**
     * bug文档
     */
    @TableField("bug_doc")
    @ExcelProperty({"测试文档", "bug文档"})
    private String bugDoc;

    /**
     * 测试报告
     */
    @TableField("report_doc")
    @ExcelProperty({"测试文档", "测试报告"})
    private String reportDoc;

    /**
     * 需求文档
     */
    @TableField("req_doc")
    @ExcelProperty(value = "是否有需求")
    private String reqDoc;

    /**
     * 完成状态（0-已完成，1-进行中，2-待回测，3-已回测）
     */
    @TableField("finish_status")
    @ExcelProperty(value = "完成状态")
    private String finishStatus;

    /**
     * 交付状态(0-是，1-否，2-不确定)
     */
    @TableField("delivery_status")
    @ExcelProperty(value = "是否交付")
    private String deliveryStatus;

    /**
     * 负责人
     */
    @TableField("tester")
    @ExcelProperty(value = "测试负责人")
    private String tester;

    /**
     * 备注
     */
    @TableField("remark")
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 任务时间
     */
    @TableField("issue_date")
    private String issueDate;

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

    @TableField(exist = false)
    private Map<String, Integer> subBug;
    @TableField(exist = false)
    private Map<String, Integer> fixBug;
    /**
     * 删除状态(0:未删除，1：已删除)
     */
    @TableField("del_flag")
    private Integer delFlag;
}
