package cn.master.tsim.controller;

import cn.master.tsim.entity.Tester;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Created by 11's papa on 2021/09/23
 * @version 1.0.0
 */
@Slf4j
@Controller
public class DefaultController {
    @RequestMapping({"", "/", "/index"})
    public String index(Model model, HttpServletRequest request) {
        Tester user = (Tester) request.getSession().getAttribute("account");
        model.addAttribute("account", user);
        return "login";
    }

    @GetMapping(value = "/dashboard")
    public String dashboard(Model model, HttpServletRequest request) {
        return "dashboard";
    }
}
