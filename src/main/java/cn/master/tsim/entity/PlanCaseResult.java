package cn.master.tsim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 11's papa
 * @since 2022-02-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_plan_case_result")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlanCaseResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 测试计划id
     */
    @TableField("plan_id")
    private String planId;

    /**
     * 测试用例id
     */
    @TableField("case_id")
    private String caseId;

    /**
     * 测试用例步骤id
     */
    @TableField("case_step_id")
    private String caseStepId;

    /**
     * 是否执行（0，未执行，1，已执行）
     */
    @TableField("run_status")
    private Boolean runStatus;

    /**
     * 执行结果
     */
    @TableField("run_result")
    private String runResult;

    /**
     * 实际结果
     */
    @TableField("execute_result")
    private String executeResult;

    /**
     * bug id
     */
    @TableField("bug_id")
    private String bugId;


}
