package cn.master.tsim.entity;

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
@TableName("test_case")
public class TestCase implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    /**
     * 测试用例标题
     */
    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @TableField("active")
    private String active;

    @TableField("project_id")
    private String projectId;

    @TableField("module_id")
    private String moduleId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("create_date")
    private Date createDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("update_date")
    private Date updateDate;
    /**
     * 备注内容
     */
    @TableField("note")
    private String note;
    /**
     * 测试前提
     */
    @TableField("precondition")
    private String precondition;

    @TableField("step_store")
    private String stepStore;
    @TableField("result_store")
    private String resultStore;
    /**
     * 优先级(0=低;1=中;2=高)
     */
    @TableField("priority")
    private String priority;
    /**
     * 测试方式(0=手动;1=自动)
     */
    @TableField("test_mode")
    private String testMode;
}
