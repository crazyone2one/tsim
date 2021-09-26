package cn.master.tsim.util;

import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.common.ResponseCode;

/**
 * @author Created by 11's papa on 2021/09/26
 * @version 1.0.0
 */
public class ResponseUtils {
    /**
     * 成功返回
     *
     * @return cn.master.tsim.common.ResponseResult
     */
    public static ResponseResult success() {
        ResponseResult resp = new ResponseResult();
        resp.setCode(ResponseCode.SUCCESS.getCode());
        resp.setMsg(ResponseCode.SUCCESS.getMessage());
        return resp;
    }

    public static ResponseResult success(Object object) {
        ResponseResult resp = new ResponseResult();
        resp.setCode(ResponseCode.SUCCESS.getCode());
        resp.setMsg(ResponseCode.SUCCESS.getMessage());
        resp.setData(object);
        return resp;
    }

    public static ResponseResult success(String message) {
        ResponseResult resp = new ResponseResult();
        resp.setCode(ResponseCode.SUCCESS.getCode());
        resp.setMsg(message);
        return resp;
    }

    public static ResponseResult success(String message, Object object) {
        ResponseResult resp = new ResponseResult();
        resp.setCode(ResponseCode.SUCCESS.getCode());
        resp.setMsg(message);
        resp.setData(object);
        return resp;
    }

    public static ResponseResult error() {
        ResponseResult resp = new ResponseResult();
        resp.setCode(ResponseCode.BODY_NOT_MATCH.getCode());
        resp.setMsg(ResponseCode.BODY_NOT_MATCH.getMessage());
        return resp;
    }

    public static ResponseResult error(String message) {
        ResponseResult resp = new ResponseResult();
        resp.setCode(ResponseCode.BODY_NOT_MATCH.getCode());
        resp.setMsg(message);
        return resp;
    }

    public static ResponseResult error(Integer code, String message, Object object) {
        ResponseResult resp = new ResponseResult();
        resp.setCode(code);
        resp.setMsg(message);
        resp.setData(object);
        return resp;
    }
}
