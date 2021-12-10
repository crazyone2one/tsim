package cn.master.tsim.exception;

import cn.master.tsim.common.ResponseCode;
import cn.master.tsim.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理
 *
 * @author Created by 11's papa on 2021/12/03
 * @version 1.0.0
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e, Model model) {
        log.error("捕获到Exception");
        log.error(e.getMessage(), e);
        model.addAttribute("result",
                ResponseUtils.error(ResponseCode.ERROR_500.getCode(), ResponseCode.ERROR_500.getMessage(), e));
        return "error";
    }

    /**
     * 空指针异常
     *
     * @param e
     * @param model
     * @return java.lang.String
     */
    @ExceptionHandler(NullPointerException.class)
    public String exceptionHandler(NullPointerException e, Model model) {
        log.error("捕获到NullPointerException");
        log.error(e.getMessage(),e);
        model.addAttribute("result",
                ResponseUtils.error(ResponseCode.PARAMS_ERROR.getCode(), ResponseCode.PARAMS_ERROR.getMessage(), e));
        return "error";
    }
}
