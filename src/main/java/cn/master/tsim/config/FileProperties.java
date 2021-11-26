package cn.master.tsim.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Created by 11's papa on 2021/11/26
 * @version 1.0.0
 */
@ConfigurationProperties(prefix = "file")
public class FileProperties {
    @Setter@Getter
    private String uploadDir;
}
