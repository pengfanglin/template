package com.fanglin.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * common包配置信息
 * @author 彭方林
 * @date 2019/4/2 14:08
 * @version 1.0
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
    private boolean requestLog=true;
    /**
     * 静态资源默认保存位置
     */
    private String staticDir;
}
