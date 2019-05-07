package com.fanglin.security;

import com.fanglin.core.others.Ajax;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;

/**
 * security登录成功处理器
 * @author 彭方林
 * @date 2019/4/3 16:35
 * @version 1.0
 **/
@Component
public class MyAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    @Qualifier("ajaxObjectMapper")
    ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Token user = (Token) authentication.getPrincipal();
        user.setAuthorities(new LinkedList<>())
            .setPassword(null);
        objectMapper.writeValue(response.getOutputStream(), Ajax.ok(user));
    }

}
