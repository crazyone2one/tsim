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
 * @since 2021-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tester")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tester implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 账号
     */
    @TableField("account")
    private String account;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 删除标记（0，未删除，1 删除）
     */
    @TableField("del_flag")
    private String delFlag;

    /**
     * 真实姓名
     */
    @TableField("username")
    private String username;


}
