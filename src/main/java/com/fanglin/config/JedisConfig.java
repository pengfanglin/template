package com.fanglin.config;

import com.fanglin.properties.JedisProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Jedis配置
 * @author 彭方林
 * @date 2019/4/3 13:04
 * @version 1.0
 **/
@Configuration
@ConditionalOnClass(JedisPool.class)
@ConditionalOnProperty(prefix = "common", name = "jedis", havingValue = "true")
@Slf4j
public class JedisConfig {
    @Autowired
    JedisProperties jedisProperties;

    @Bean
    public JedisPool redisPoolFactory() {
        log.info("jedisPool开启成功");
        return new JedisPool(jedisPoolConfig(), jedisProperties.getHost(), jedisProperties.getPort(), jedisProperties.getTimeout(), jedisProperties.getPassword(), jedisProperties.getDatabase());
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(jedisProperties.getMaxIdle());
        jedisPoolConfig.setMinIdle(jedisProperties.getMinIdle());
        jedisPoolConfig.setMaxTotal(jedisProperties.getMaxTotal());
        return jedisPoolConfig;
    }
}