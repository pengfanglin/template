package com.fanglin.security;

import com.fanglin.core.others.Ajax;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * security登录失败处理器
 * @author 彭方林
 * @date 2019/4/3 16:35
 * @version 1.0
 **/
@Component
public class MyAuthFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    @Qualifier("ajaxObjectMapper")
    ObjectMapper objectMapper;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Ajax ajax;
        if(e instanceof BadCredentialsException){
            ajax=Ajax.error("账户或密码错误");
        }else if(e instanceof DisabledException){
            ajax=Ajax.error("账号已禁用");
        }else{
            ajax=Ajax.error(e.getMessage());
        }
        objectMapper.writeValue(response.getOutputStream(), ajax);
    }
}