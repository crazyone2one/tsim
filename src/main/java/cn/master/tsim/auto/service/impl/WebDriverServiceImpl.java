package cn.master.tsim.auto.service.impl;

import cn.master.tsim.auto.service.WebDriverService;
import cn.master.tsim.config.DriverServerProperties;
import cn.master.tsim.config.WebDriverProperties;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

/**
 * @author by 11's papa on 2022年01月19日
 * @version 1.0.0
 */
@Slf4j
@Component
public class WebDriverServiceImpl implements WebDriverService {
    @Qualifier("webDriverProperties")
    @Autowired
    private WebDriverProperties driverProperties;

    @Qualifier("driverServerProperties")
    @Autowired
    private DriverServerProperties serverProperties;

    @Override
    public RemoteWebDriver initializeWebDriver() {
        log.info("准备初始化WebDriver对象...");
        DesiredCapabilities capabilities = new DesiredCapabilities(driverProperties.getCapabilities());
        String tempUrl = serverProperties.getLocation() + ":" + serverProperties.getPort() + "/wd/hub";
        RemoteWebDriver remoteWebDriver = null;
        try {
            remoteWebDriver = new RemoteWebDriver(new URL(tempUrl), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        assert remoteWebDriver != null;
        remoteWebDriver.manage().window().maximize();
        remoteWebDriver.get(serverProperties.getTestServer());
        //设置元素出现等待时长10秒
        remoteWebDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        // 设置页面加载最大时长10秒
        remoteWebDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        return remoteWebDriver;
    }

    @Override
    public WebElement findElement(WebDriver driver, String property, String propertyContent) {
        WebElement element;
        try {
            switch (property.toLowerCase()) {
                default:
                case "id":
                    element = driver.findElement(By.id(propertyContent));
                    break;
                case "name":
                    element = driver.findElement(By.name(propertyContent));
                    break;
                case "xpath":
                    element = driver.findElement(By.xpath(propertyContent));
                    break;
                case "linktext":
                    element = driver.findElement(By.linkText(propertyContent));
                    break;
            }
            return element;
        } catch (NoSuchElementException e) {
            log.error("元素对象定位失败", e);
            return null;
        }
    }

    @Override
    public List<WebElement> findElements(WebDriver driver, String property, String propertyContent) {
        List<WebElement> result = new LinkedList<>();

        try {
            switch (property.toLowerCase()) {
                default:
                case "id":
                    result = driver.findElements(By.id(propertyContent));
                    break;
                case "name":
                    result = driver.findElements(By.name(propertyContent));
                    break;
                case "xpath":
                    result = driver.findElements(By.xpath(propertyContent));
                    break;
                case "linktext":
                    result = driver.findElements(By.linkText(propertyContent));
                    break;
            }
            return result;
        } catch (NoSuchElementException e) {
            log.error("元素对象定位失败", e);
            return result;
        }
    }

    @Override
    public void elementOperation(WebElement element, String operation, String operationContent) {
        switch (operation) {
            default:
            case "click":
                element.click();
                break;
            case "sendkeys":
                element.clear();
                element.sendKeys(operationContent);
                break;
        }
    }
}
