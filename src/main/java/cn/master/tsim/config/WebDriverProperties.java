package cn.master.tsim.config;

import lombok.Data;
import org.openqa.selenium.remote.BrowserType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author by 11's papa on 2022年01月19日
 * @version 1.0.0
 */
@Data
@Component("webDriverProperties")
@ConfigurationProperties(prefix = "webdriver")
public class WebDriverProperties {
    private String osName = System.getProperty("os.name");
    private String browserName = BrowserType.CHROME;
    private boolean isRemote = true;
    private Map<String, Object> capabilities;
    private String[] listeners;
}
