package com.fanglin.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * common包配置信息
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/2 14:08
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "common")
@Component
public class CommonProperties {
    /**
     * 请求日志 默认开启
     */
    private boolean requestLog = false;
    /**
     * redis自动配置 默认关闭
     */
    private boolean redis = false;
    /**
     * jedis自动配置 默认关闭
     */
    private boolean jedis = false;
    /**
     * httpClient自动配置 默认开启
     */
    private boolean http = false;
    /**
     * 静态文件保存目录
     */
    private String staticDir;
}
