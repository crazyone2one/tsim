package cn.master.tsim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 项目-测试用例关联表
 * </p>
 *
 * @author 11's papa
 * @since 2021-11-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_project_case_ref")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCaseRef implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    /**
     * projectId
     */
    @TableField("project_id")
    private String projectId;

    /**
     * storyId
     */
    @TableField("story_id")
    private String storyId;
    @TableField("plan_id")
    private String planId;

    /**
     * caseId
     */
    @TableField("case_id")
    private String caseId;

    /**
     * workDate
     */
    @TableField("work_date")
    private String workDate;


}
