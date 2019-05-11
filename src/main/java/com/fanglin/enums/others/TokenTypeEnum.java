package com.fanglin.enums.others;

import com.fanglin.core.others.ValidateException;

/**
 * 支付通知
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/10 19:40
 **/
public enum TokenTypeEnum {
    //令牌类型
    USER("user"),
    ADMIN("admin");

    private String value;

    TokenTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TokenTypeEnum find(String value) {
        for (TokenTypeEnum tokenTypeEnum : TokenTypeEnum.values()) {
            if (tokenTypeEnum.value.equals(value)) {
                return tokenTypeEnum;
            }
        }
        throw new ValidateException("枚举类型不存在");
    }
}