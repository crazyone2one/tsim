package cn.master.tsim;

import cn.master.tsim.config.FileProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author jingll
 */
@SpringBootApplication
@MapperScan("cn.master.tsim.mapper")
@EnableConfigurationProperties({FileProperties.class})
public class TsimApplication {

    public static void main(String[] args) {
        SpringApplication.run(TsimApplication.class, args);
    }

}
