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
@TableName("t_module")
public class Module implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 模块名称
     */
    @TableField("module_name")
    private String moduleName;

    /**
     * 模块code
     */
    @TableField("module_code")
    private String moduleCode;

    /**
     * 项目id
     */
    @TableField("project_id")
    private String projectId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("create_date")
    private Date createDate;

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
    private List<TestCase> caseList;
}
