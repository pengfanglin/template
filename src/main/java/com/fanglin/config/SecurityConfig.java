package com.fanglin.config;

import com.fanglin.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.cors.CorsUtils;

/**
 * spring security配置 默认禁用注解控制权限，需要加以下注解手动开启
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:39
 **/
@Configuration
@EnableWebSecurity
@EnableRedisHttpSession
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;
    @Autowired
    private MyAuthExceptionEntryPoint myAuthExceptionEntryPoint;
    @Autowired
    private MyAuthFailureHandler myAuthFailureHandler;
    @Autowired
    private MyAuthSuccessHandler myAuthSuccessHandler;
    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    /**
     * 自定义授权规则
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
            .and()
            //禁用防跨站伪请求攻击，默认启用
            .csrf().disable()
            .httpBasic()
            .and()
            .exceptionHandling()
            //认证异常处理器
            .authenticationEntryPoint(myAuthExceptionEntryPoint)
            //无权限处理器
            .accessDeniedHandler(myAccessDeniedHandler)
            .and()
            .authorizeRequests()
            //解决跨域问题
            .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
            //新登录把旧session顶替掉，需要放行该跳转路径，否则会提示401未授权
            .antMatchers("/**", "/error/**", "/auth/login", "/auth/expired", "/auth/invalid").permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .logout()
            .logoutUrl("/auth/logout")
            //注销成功过滤器
            .logoutSuccessHandler(myLogoutSuccessHandler)
            .and()
            .rememberMe()
            //请求中带该参数的，会记住登录
            .rememberMeParameter("remember")
            .and()
            .formLogin()
            .loginProcessingUrl("/auth/login")
            //登录成功处理器
            .successHandler(myAuthSuccessHandler)
            //登录失败处理器
            .failureHandler(myAuthFailureHandler)
            .and()
            .sessionManagement()
            //限制同一用户只能在一个地点登录
            .maximumSessions(1)
            //多次登录被踢出时的跳转路径
            .expiredUrl("/auth/expired")
            .and()
            .invalidSessionUrl("/auth/invalid");
    }

    /**
     * 定义认证规则
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * 这里有个小坑，如果服务器用的是云服务器，不加这个会报错
     *
     * @return
     */
    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }
}

