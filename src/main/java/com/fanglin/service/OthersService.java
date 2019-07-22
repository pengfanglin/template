package com.fanglin.service;

import com.fanglin.core.others.Ajax;


/**
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/4 10:00
 **/
public interface OthersService {
    /**
     * 发送验证码
     *
     * @param mobile 手机号
     * @return
     */
    Ajax sendCode(String mobile);

}
