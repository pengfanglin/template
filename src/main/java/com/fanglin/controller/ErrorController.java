package com.fanglin.controller;

import com.fanglin.core.others.Ajax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 替换springboot默认的error处理器
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 14:15
 **/
@RestController
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    private static final String PATH = "/error";
    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping(value = PATH, produces = {MediaType.APPLICATION_JSON_VALUE})
    Ajax error(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> errorAttributes = getErrorAttributes(request);
        int code = (int) errorAttributes.get("status");
        String path = errorAttributes.get("path").toString();
        String message = errorAttributes.get("message").toString();
        return Ajax.status(false, code, String.format("【%s】 %s", path, message));
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request) {
        return errorAttributes.getErrorAttributes(new ServletWebRequest(request), false);
    }
}
