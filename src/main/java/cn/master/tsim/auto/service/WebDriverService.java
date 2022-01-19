package cn.master.tsim.auto.service;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

/**
 * @author by 11's papa on 2022年01月19日
 * @version 1.0.0
 */
public interface WebDriverService {
    /**
     * description: 初始化webdriver <br>
     *
     * @return org.openqa.selenium.remote.RemoteWebDriver
     * @author 11's papa
     */
    RemoteWebDriver initializeWebDriver();

    WebElement findElement(WebDriver driver, String property, String propertyContent);
    List<WebElement> findElements(WebDriver driver, String property, String propertyContent);

    void elementOperation(WebElement element, String operation, String operationContent);
}
