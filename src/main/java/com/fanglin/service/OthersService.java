package com.fanglin.service;

import com.fanglin.entity.others.CodeEntity;


/**
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/4 10:00
 **/
public interface OthersService {
    /**
     * 添加新的验证码
     *
     * @param code
     * @return
     */
    int insertCode(CodeEntity code);

}
