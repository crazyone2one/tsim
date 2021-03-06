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
 * 需求表
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_story")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestStory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 需求内容
     */
    @TableField("description")
    private String description;
    /**
     * 需求名称
     */
    @TableField("story_name")
    private String storyName;

    /**
     * 项目id
     */
    @TableField("project_id")
    private String projectId;


    /**
     * 需求时间
     */
    @TableField("work_date")
    private String workDate;
    @TableField("doc_id")
    private String docId;
    @TableField(exist = false)
    private DocInfo docInfo;
    /**
     * 删除状态(0:未删除，1：已删除)
     */
    @TableField("del_flag")
    private Integer delFlag;

    @TableField("create_date")
    private Date createDate;

    @TableField("update_date")
    private Date updateDate;

    @TableField(exist = false)
    private Project project;
    @TableField("story_status")
    private Integer storyStatus;
}
