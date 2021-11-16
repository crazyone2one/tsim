package cn.master.tsim.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Created by 11's papa on 2021/09/23
 * @version 1.0.0
 */
@Getter
@AllArgsConstructor
public enum ResponseCode {
    // 数据操作错误定义
    SUCCESS(200, "成功!"),
    BODY_NOT_MATCH(400, "请求的数据格式不符!"),
    ERROR_404(404, "未找到该资源!"),
    PARAMS_ERROR(403, "参数错误!"),
    ERROR_500(500, "服务器内部错误!"),
    SERVER_BUSY(503, "服务器正忙，请稍后再试!");

    private final Integer code;
    private final String message;
}
