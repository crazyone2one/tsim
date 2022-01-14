package cn.master.tsim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 测试计划与测试需求关联关系表
 * </p>
 *
 * @author 11's papa
 * @since 2022-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_plan_story_ref")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlanStoryRef implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value="id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 测试计划数据id
     */
    @TableField("plan_id")
    private String planId;

    /**
     * 需求数据id
     */
    @TableField("story_id")
    private String storyId;


}
