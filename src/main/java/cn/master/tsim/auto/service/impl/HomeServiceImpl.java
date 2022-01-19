package cn.master.tsim.auto.service.impl;

import cn.master.tsim.auto.service.HomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author by 11's papa on 2022年01月19日
 * @version 1.0.0
 */
@Slf4j
@Service("homeServiceImpl")
public class HomeServiceImpl extends LoginServiceImpl implements HomeService {

    @Override
    public boolean validateLoginSuccess() {
        log.info("验证登录操作");
        return driver.findElementById("iframeId").isDisplayed();
    }
}
