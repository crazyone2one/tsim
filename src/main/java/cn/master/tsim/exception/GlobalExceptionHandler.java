package cn.master.tsim.exception;

import cn.master.tsim.common.ResponseCode;
import cn.master.tsim.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

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
    public String exceptionHandler(HttpServletResponse response, Exception e, Model model) {
        log.error(e.getLocalizedMessage());
        model.addAttribute("error",
                ResponseUtils.error(ResponseCode.ERROR_500.getCode(),ResponseCode.ERROR_500.getMessage(),e));
        return "error";
    }
}
