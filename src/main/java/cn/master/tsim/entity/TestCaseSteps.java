package cn.master.tsim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("t_case_steps")
public class TestCaseSteps implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    @TableField("case_id")
    private String caseId;

    @TableField("case_order")
    private Integer caseOrder;

    @TableField("case_step")
    private String caseStep;

    @TableField("case_step_result")
    private String caseStepResult;

    @TableField("active")
    private Integer active;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("create_date")
    private Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("update_date")
    private Date updateDate;


}
