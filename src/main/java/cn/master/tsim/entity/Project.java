package cn.master.tsim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 项目表
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
@TableName("t_project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 项目名称
     */
    @TableField("project_name")
    private String projectName;

    /**
     * 项目code
     */
    @TableField("project_code")
    private String projectCode;
    /**
     * 状态标志(0=正常;1=无效)
     */
    @TableField("project_status")
    private Integer projectStatus;

    @TableField("work_date")
    private String workDate;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("create_data")
    private Date createData;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("update_date")
    private Date updateDate;

    /**
     * 删除标志(0=未删除;1=删除)
     */
    @TableField("del_flag")
    private Integer delFlag;
    @TableField(exist = false)
    private String finishStatus;

    @TableField(exist = false)
    private List<Module> modules;

    @TableField(exist = false)
    private List<TestTaskInfo> projectTasks;
    @TableField(exist = false)
    private String issueDate;
    @TableField(exist = false)
    private Integer refModuleCount;
    @TableField(exist = false)
    private Integer refCaseCount;
    @TableField(exist = false)
    private Integer refBugCount;
    @TableField(exist = false)
    private boolean reportDoc;
}
