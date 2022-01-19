package cn.master.tsim.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author by 11's papa on 2022年01月19日
 * @version 1.0.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "webdriver.service")
public class DriverServerProperties {
    private String port = "4444";
    private String location = "http://localhost";
    private String defaultUsername;
    private String defaultPassword;
    private String testServer;
}
