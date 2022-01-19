package cn.master.tsim.auto.service;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author by 11's papa on 2022年01月19日
 * @version 1.0.0
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService{
    private final RemoteWebDriver driver;

    @Autowired
    public LoginServiceImpl(RemoteWebDriver remoteWebDriver) {
        this.driver = remoteWebDriver;
    }

    @Override
    public void doLogin() {
        driver.findElementById("userName").sendKeys("001");
    }
}
