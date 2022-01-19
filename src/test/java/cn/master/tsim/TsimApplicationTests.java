package cn.master.tsim;

import cn.master.tsim.config.DriverServerProperties;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class TsimApplicationTests {
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    protected JdbcTemplate jdbcTemplate1;

    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    protected JdbcTemplate jdbcTemplate2;
    @Autowired
    protected RemoteWebDriver driver;
    @Autowired
    private DriverServerProperties driverServerProperties;
    @Test
    void contextLoads() {
        driver.get(driverServerProperties.getTestServer());
        driver.quit();
    }

}
