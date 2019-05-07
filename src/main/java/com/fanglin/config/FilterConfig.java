package com.fanglin.config;

import com.fanglin.core.filter.RequestLogFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 过滤器配置
 * @author 彭方林
 * @date 2019/4/2 13:35
 * @version 1.0
 **/
@Configuration
public class FilterConfig {

    /**
     * 打印请求日志
     */
    @Bean
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @ConditionalOnProperty(prefix = "common",name = "requestLog",havingValue = "true",matchIfMissing = true)
    public FilterRegistrationBean filterRegister() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new RequestLogFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}
