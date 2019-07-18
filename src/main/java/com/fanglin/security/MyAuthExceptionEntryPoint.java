package com.fanglin.security;

import com.fanglin.core.others.Ajax;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * security未授权处理器
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:35
 **/
@Component
public class MyAuthExceptionEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    @Qualifier("ajaxObjectMapper")
    ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        System.out.println(authException.getClass().toString() + " " + authException.getMessage());
        objectMapper.writeValue(response.getOutputStream(), Ajax.status( 401, "未授权，请登录"));
    }
}
