package cn.master.tsim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

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
@TableName("t_case_review_ref")
public class CaseReviewRef implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    @TableField("review_id")
    private String reviewId;

    @TableField("case_id")
    private String caseId;

    @TableField("review_status")
    private Boolean reviewStatus;

    @TableField("review_content")
    private String reviewContent;


}
