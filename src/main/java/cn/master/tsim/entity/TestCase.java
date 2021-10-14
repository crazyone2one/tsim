package cn.master.tsim.entity;

import com.alibaba.excel.annotation.ExcelProperty;
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
@TableName("test_case")
public class TestCase implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 测试用例标题
     */
    @TableField("name")
    @ExcelProperty(value = "测试用例标题", index = 2)
    private String name;

    @TableField("description")
    @ExcelProperty(value = "摘要", index = 3)
    private String description;

    @TableField("active")
    private String active;

    @TableField("project_id")
    @ExcelProperty(value = "项目名称", index = 0)
    private String projectId;

    @TableField("module_id")
    @ExcelProperty(value = "模块", index = 1)
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
    @ExcelProperty(value = "前提条件", index = 4)
    private String precondition;

    @TableField("step_store")
    @ExcelProperty(value = "测试步骤", index = 5)
    private String stepStore;
    @TableField("result_store")
    @ExcelProperty(value = "预期结果", index = 6)
    private String resultStore;
    /**
     * 优先级(0=低;1=中;2=高)
     */
    @TableField("priority")
    @ExcelProperty(value = "优先级", index = 7)
    private String priority;
    /**
     * 测试方式(0=手动;1=自动)
     */
    @TableField("test_mode")
    private String testMode;
}
