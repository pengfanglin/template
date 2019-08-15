package com.fanglin.service;


import com.fanglin.enums.others.CodeType;

/**
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/4 10:00
 **/
public interface OthersService {
    /**
     * 添加新的验证码
     *
     * @param mobile 手机号
     * @param type   类型
     */
    void sendCode(String mobile, CodeType type);

}
