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
 * 
 * </p>
 *
 * @author 11's papa
 * @since 2022-02-09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("t_case_review")
public class CaseReview implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 评审任务名称
     */
    @TableField("review_name")
    private String reviewName;

    /**
     * 评审人
     */
    @TableField("review_user")
    private String reviewUser;

    /**
     * 评审任务内容
     */
    @TableField("review_remark")
    private String reviewRemark;

    /**
     * 结束时间
     */
    @TableField("finish_date")
    private Date finishDate;
    /**
     * 完成状态
     */
    @TableField("finish_status")
    private Integer finishStatus;

    @TableField("create_date")
    private Date createDate;

    @TableField("update_date")
    private Date updateDate;


}
