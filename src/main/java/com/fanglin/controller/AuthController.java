package com.fanglin.controller;

import com.fanglin.core.others.Ajax;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 14:15
 **/
@RestController
@RequestMapping("/auth/")
public class AuthController {
    /**
     * 多个地点登录被踢出
     *
     * @return
     */
    @RequestMapping("expired")
    public Ajax expired() {
        return Ajax.status(false,600, "已在其他地点登录!");
    }

    /**
     * session无效
     *
     * @return
     */
    @RequestMapping("invalid")
    public Ajax invalid() {
        return Ajax.error("授权信息无效!");
    }
}
