package cn.master.tsim.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Created by 11's papa on 2021/09/26
 * @version 1.0.0
 */
@Data
public class ResponseResult implements Serializable {
    private static final long serialVersionUID = 3595741978061989861L;
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 状态码对应信息
     */
    private String msg;
    /**
     * 要返回的数据
     */
    private Object data;
}
