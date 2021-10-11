package cn.master.tsim.controller;


import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.Tester;
import cn.master.tsim.service.TesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2021-10-11
 */
@Controller
@RequestMapping("")
public class TesterController {

    private final TesterService testerService;

    @Autowired
    public TesterController(TesterService testerService) {
        this.testerService = testerService;
    }

    @RequestMapping("/login")
    public String login(Tester user, Model model) {
        final ResponseResult responseResult = testerService.login(user);
        if (Objects.equals(200, responseResult.getCode())) {
            return "redirect:/dashboard";
        } else {
            model.addAttribute("msg", responseResult.getMsg());
            return "login";
        }
    }

    @RequestMapping("/register")
    public String register(Tester user) {
        final ResponseResult responseResult = testerService.register(user);
        return "login";
    }
}

