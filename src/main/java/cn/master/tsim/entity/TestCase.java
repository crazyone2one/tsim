package cn.master.tsim.entity;

import cn.master.tsim.annotation.ParamCheck;
import com.alibaba.excel.annotation.ExcelProperty;
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
@TableName("t_case")
public class TestCase implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 测试用例标题
     */
    @ParamCheck(errorRequiredMessage = "测试用例标题不能为空")
    @TableField("name")
    @ExcelProperty(value = "测试用例标题", index = 2)
    private String name;

    @TableField("description")
    @ExcelProperty(value = "摘要", index = 3)
    private String description;
    /**
     * 删除标志(0=未删除;1=删除)
     */
    @TableField("active")
    private Integer active;

    @ParamCheck(required = true, errorRequiredMessage = "项目名称不能为空")
    @TableField("project_id")
    @ExcelProperty(value = "项目名称", index = 0)
    private String projectId;

    @TableField(exist = false)
    private Project project;

    @ParamCheck(required = true, errorRequiredMessage = "模块名称不能为空")
    @TableField("module_id")
    @ExcelProperty(value = "模块", index = 1)
    private String moduleId;
    @TableField(exist = false)
    private String storyId;
    @TableField(exist = false)
    private Module module;

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

    @ParamCheck(required = true, errorRequiredMessage = "测试步骤不能为空")
    @TableField("step_store")
    @ExcelProperty(value = "测试步骤", index = 5)
    private String stepStore;

    @ParamCheck(required = true, errorRequiredMessage = "预期结果不能为空")
    @TableField("result_store")
    @ExcelProperty(value = "预期结果", index = 6)
    private String resultStore;
    /**
     * 优先级(0=低;1=中;2=高)
     */
    @TableField("priority")
    @ExcelProperty(value = "优先级", index = 7)
    private Integer priority;
    /**
     * 测试方式(0=手动;1=自动)
     */
    @TableField("test_mode")
    private Integer testMode;

    @TableField("run_mode_manual")
    private String runModeManual;

    @TableField("run_mode_auto")
    private String runModeAuto;

    @TableField(exist = false)
    private List<TestCaseSteps> caseSteps;

    @TableField(exist = false)
    @ExcelProperty(value = "是否已导入", index = 8)
    private boolean refFlag;

    /**
     * 删除状态（0，有效 1，无效）
     */
    @TableField("del_flag")
    private Integer delFlag;
}
