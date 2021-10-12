package cn.master.tsim.entity;

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
@TableName("test_task_info")
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
    @TableField("project_id")
    private String projectId;

    /**
     * 任务描述
     */
    @TableField("summary_desc")
    private String summaryDesc;

    /**
     * 编写用例数量
     */
    @TableField("create_case_count")
    private Integer createCaseCount;

    /**
     * 执行测试用例数量
     */
    @TableField("execute_case_count")
    private Integer executeCaseCount;

    /**
     * bug文档
     */
    @TableField("bug_doc")
    private String bugDoc;

    /**
     * 测试报告
     */
    @TableField("report_doc")
    private String reportDoc;

    /**
     * 需求文档
     */
    @TableField("req_doc")
    private String reqDoc;

    /**
     * 完成状态
     */
    @TableField("finish_status")
    private String finishStatus;

    /**
     * 交付状态
     */
    @TableField("delivery_status")
    private String deliveryStatus;

    /**
     * 负责人
     */
    @TableField("tester")
    private String tester;

    /**
     * 备注
     */
    @TableField("remark")
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
}
