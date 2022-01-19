package cn.master.tsim.auto.service.impl;

import cn.master.tsim.auto.service.HomeService;
import cn.master.tsim.auto.service.LoginService;
import cn.master.tsim.auto.service.WebDriverService;
import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author by 11's papa on 2022年01月19日
 * @version 1.0.0
 */
@Slf4j
@Service("loginServiceImpl")
public class LoginServiceImpl implements LoginService {
    RemoteWebDriver driver;
    @Autowired
    private WebDriverService service;
    @Autowired
    private HomeService homeService;

    @Override
    public ResponseResult doLogin() {
        try {
            driver = service.initializeWebDriver();
        } catch (Exception e) {
            log.error("初始化WebDriver出错", e);
            return ResponseUtils.error("初始化WebDriver出错");
        }
        log.info("开始执行用例");
        try {
            WebElement userName = service.findElement(driver, "id", "userName");
            service.elementOperation(userName, "sendkeys", "001");
            service.elementOperation(service.findElement(driver, "id", "password"), "sendkeys", "123456");
            service.elementOperation(service.findElement(driver,"id","but_login"),"click","");
            if (homeService.validateLoginSuccess()) {
                driver.quit();
                return ResponseUtils.success();
            } else {
                driver.quit();
                return ResponseUtils.success("执行失败");
            }
        } catch (Exception e) {
            driver.quit();
            return ResponseUtils.error(e.getMessage());
        }
    }
}
