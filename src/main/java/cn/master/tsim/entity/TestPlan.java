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

/**
 * <p>
 * 测试计划表
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("test_plan")
@Builder
@AllArgsConstructor
public class TestPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 测试计划名称
     */
    @TableField("name")
    private String name;

    /**
     * 测试计划描述
     */
    @TableField("description")
    private String description;

    /**
     * 项目id
     */
    @TableField("project_id")
    private String projectId;

    /**
     * 需求id
     */
    @TableField("story_id")
    private String storyId;

    /**
     * 删除状态（0，有效 1，无效）
     */
    @TableField("del_flag")
    private String delFlag;

    @TableField("group_id")
    private String groupId;

    @TableField("num")
    private String num;


}
