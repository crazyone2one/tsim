package cn.master.tsim.config;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author by 11's papa on 2022年01月19日
 * @version 1.0.0
 */
@Configuration
@EnableConfigurationProperties({WebDriverProperties.class, DriverServerProperties.class})
@ConditionalOnMissingBean(name = {"driverService"})
@ConditionalOnClass(DriverService.class)
public class DriverConfig {
    private final WebDriverProperties webDriverProperties;
    private final DriverServerProperties driverServerProperties;

    @Autowired
    public DriverConfig(WebDriverProperties webDriverProperties, DriverServerProperties driverServerProperties) {
        this.webDriverProperties = webDriverProperties;
        this.driverServerProperties = driverServerProperties;
    }

    @Bean
    public RemoteWebDriver remoteWebDriver() {
        DesiredCapabilities capabilities = new DesiredCapabilities(webDriverProperties.getCapabilities());
        String tempUrl = driverServerProperties.getLocation() + ":" + driverServerProperties.getPort() + "/wd/hub";
        RemoteWebDriver remoteWebDriver = null;
        try {
            remoteWebDriver = new RemoteWebDriver(new URL(tempUrl), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        assert remoteWebDriver != null;
        remoteWebDriver.manage().window().maximize();
        remoteWebDriver.get(driverServerProperties.getTestServer());
        return remoteWebDriver;
    }
}
