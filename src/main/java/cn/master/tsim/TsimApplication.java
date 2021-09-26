package cn.master.tsim;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jingll
 */
@SpringBootApplication
@MapperScan("cn.master.tsim.mapper")
public class TsimApplication {

    public static void main(String[] args) {
        SpringApplication.run(TsimApplication.class, args);
    }

}
