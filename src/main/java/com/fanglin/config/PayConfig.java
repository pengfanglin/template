package com.fanglin.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.fanglin.properties.PayProperties;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * 支付配置
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/2 13:35
 **/
@Configuration
public class PayConfig {
    @Autowired
    PayProperties payProperties;

    /**
     * 支付宝请求客户端
     */
    @Bean
    @ConditionalOnProperty("pay.alipay.app-id")
    public AlipayClient getAlipayClient() {
        return new DefaultAlipayClient(
            "https://openapi.alipay.com/gateway.do",
            payProperties.getAlipay().getAppId(),
            payProperties.getAlipay().getSecret(),
            "json",
            "UTF-8",
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhc407Wydnnes1MRqNb7JCUXCwxfIMvRyGDwT6MGrEDb0ZZAjgFiZO/IzV+vwpkttgbrYUaxjdrsVSir0O+q/dywndDo/hCEEPcxdKqyoGxIazRgGYIZqRLSOfNMI14Tt0WN08Uwqs8SvqE3e4xLZmiW+oIT5NGmInNkKyVVDckhO1hENydAj3szb4YTSrxUa12+Tbgkt1cgxKoILI7uXQwdcps9c/goQdSd0v1FFyuifO1Z2mcbYr+KP3agHS6JuP1JDmDAAY6XAtHHnm+5lS2tGiQ8iHdwNoowNGv5oaRAtRIJBQnccG2bv0ItnLtIFdnS8qI8C+UircJbJpcRLEQIDAQAB",
            "RSA2"
        );
    }

    /**
     * http-client配置
     */
    @Bean("wxHttpClient")
    @ConditionalOnProperty("pay.wx.app-id")
    public CloseableHttpClient wxHttpClient() throws Exception {
        return buildHttpClient(payProperties.getWx().getHttpClient(), payProperties.getWx().getMchId());
    }

    /**
     * 构建httpClient
     *
     * @param properties 配置文件
     * @param mchId      商户id(加载证书)
     * @return
     * @throws Exception
     */
    private CloseableHttpClient buildHttpClient(PayProperties.HttpClientProperties properties, String mchId) throws Exception {
        //指定证书类型
        KeyStore wxKeyStore = KeyStore.getInstance("PKCS12");
        // 读取本机存放的PKCS12证书文件
        ClassPathResource classPathResource = new ClassPathResource(properties.getCertificateName());
        InputStream inputStream = classPathResource.getInputStream();
        // 加载微信支付商户证书
        wxKeyStore.load(inputStream, mchId.toCharArray());
        //根据证书生成SSL上下文
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(wxKeyStore, mchId.toCharArray()).build();
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier.INSTANCE);
        //指定http-client请求参数
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.getSocketFactory())
            .register("https", sslConnectionSocketFactory)
            .build();
        PoolingHttpClientConnectionManager gcm = new PoolingHttpClientConnectionManager(registry);
        gcm.setMaxTotal(properties.getMaxTotal());
        gcm.setDefaultMaxPerRoute(properties.getDefaultMaxPerRoute());
        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(properties.getConnectionRequestTimeout())
            .setConnectTimeout(properties.getConnectTimeout())
            .setSocketTimeout(properties.getSocketTimeout())
            .build();
        return HttpClients.custom().setConnectionManager(gcm).setDefaultRequestConfig(requestConfig).build();
    }
}
