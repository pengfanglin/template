package com.fanglin.enums.others;

import lombok.Getter;

/**
 * 验证码类型
 * @author 彭方林
 * @version 1.0
 * @date 2019/5/16 20:27
 **/
public enum CodeType {

    /**
     * 用户注册
     */
    USER_REGISTER(0),
    /**
     * 找回密码
     */
    FIND_PASSWORD(1),
    /**
     * 修改密码
     */
    UPDATE_PASSWORD(2);

    @Getter
    private int code;

    CodeType(int code){
        this.code=code;
    }

    public static CodeType find(int code){
        for(CodeType codeType: CodeType.values()){
            if(codeType.getCode()==code){
                return codeType;
            }
        }
        return null;
    }
}
