package com.fanglin.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 支付配置类
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/2 14:30
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "pay")
@Component
public class PayProperties {
    /**
     * 支付宝app
     */
    private AlipayProperties alipay;
    /**
     * 微信app
     */
    private WxProperties wx;

    @Data
    public static class AlipayProperties {
        /**
         * 应用id
         */
        private String appId;
        /**
         * 密钥，即appsecret
         */
        private String secret;
    }

    @Data
    public static class WxProperties {
        /**
         * 应用id
         */
        private String appId;
        /**
         * 商户id
         */
        private String mchId;
        /**
         * 密钥，即appsecret
         */
        private String secret;
        /**
         * 微信退款httpClient
         */
        private HttpClientProperties httpClient;
    }

    @Data
    public static class HttpClientProperties {
        /**
         * 从连接池获取到连接的超时时间
         */
        private int connectionRequestTimeout = 5000;
        /**
         * 建立连接的超时时间
         */
        private int connectTimeout = 2000;
        /**
         * 客户端和服务进行数据交互的超时时间
         */
        private int socketTimeout = 2000;
        /**
         * 最大连接数
         */
        private int maxTotal = 200;
        /**
         * 连接池按route配置的最大连接数(每个请求地址的最大连接数，defaultMaxPerRoute*目标地址<=maxTotal)
         */
        private int defaultMaxPerRoute = 200;
        /**
         * 证书名称
         */
        private String certificateName;
    }
}