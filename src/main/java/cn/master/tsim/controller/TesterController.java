package cn.master.tsim.controller;


import cn.master.tsim.common.ResponseCode;
import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.Tester;
import cn.master.tsim.service.TesterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2021-10-11
 */
@Slf4j
@Controller
@RequestMapping("user")
public class TesterController {

    private final TesterService testerService;

    @Autowired
    public TesterController(TesterService testerService) {
        this.testerService = testerService;
    }

    @RequestMapping("/login")
    public String login(Tester user, Model model, HttpServletRequest request) {
        if (StringUtils.isBlank(user.getAccount()) && StringUtils.isBlank(user.getPassword())) {
            return "login";
        }
        final ResponseResult responseResult = testerService.login(user);
        if (Objects.equals(ResponseCode.SUCCESS.getCode(), responseResult.getCode())) {
            String requestURI = request.getRequestURI();
            log.debug("此次请求的url:{}", requestURI);
            HttpSession session = request.getSession();
            log.debug("session=" + session + "session.getId()=" + session.getId() + "session.getMaxInactiveInterval()=" + session.getMaxInactiveInterval());
            request.getSession().setAttribute("account", responseResult.getData());
            return "redirect:/dashboard";
        }
        model.addAttribute("msg", responseResult.getMsg());
        return "login";
    }

    @RequestMapping("/register")
    public String register(Tester user) {
        final ResponseResult responseResult = testerService.register(user);
        return "login";
    }

    @RequestMapping("/exit")
    public String exit(HttpServletRequest request) {
        request.getSession().removeAttribute("account");
        log.debug("进入exit方法，移除account");
        return "redirect:/user/login";
    }
}

