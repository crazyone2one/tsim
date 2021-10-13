package cn.master.tsim.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Created by 11's papa on 2021/10/12
 * @version 1.0.0
 */
@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {"/user/login", "/",
            "/css/**", "/js/**", "/icons/**","/favicon.ico"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(CLASSPATH_RESOURCE_LOCATIONS);
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
