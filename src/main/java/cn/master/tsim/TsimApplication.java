package cn.master.tsim;

import cn.master.tsim.config.FileProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author jingll
 */
@SpringBootApplication
@MapperScan("cn.master.tsim.mapper")
@EnableConfigurationProperties({FileProperties.class})
public class TsimApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(TsimApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TsimApplication.class);
    }
}
