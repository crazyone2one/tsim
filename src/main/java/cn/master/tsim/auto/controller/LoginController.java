package cn.master.tsim.auto.controller;

import cn.master.tsim.auto.service.LoginService;
import cn.master.tsim.common.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by 11's papa on 2022年01月19日
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/auto")
public class LoginController {
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @RequestMapping("/loginMainProcess")
    public ResponseResult loginMainProcess() {
        loginService.doLogin();
        return null;
    }
}
