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
 * @since 2021-10-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_plan_case_ref")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlanCaseRef implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 测试计划id
     */
    @TableField("plan_id")
    private String planId;

    @TableField(exist = false)
    private TestPlan plan;

    /**
     * 测试用例id
     */
    @TableField("case_id")
    private String caseId;

    @TableField(exist = false)
    private TestCase testCase;

    /**
     * 是否执行（0，未执行，1，已执行）
     */
    @TableField("run_status")
    private Integer runStatus;

    /**
     * 执行结果（0，通过，1，未通过）
     */
    @TableField("run_result")
    private Integer runResult;

    /**
     * bug id
     */
    @TableField("bug_id")
    private String bugId;


}
