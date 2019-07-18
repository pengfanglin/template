package com.fanglin.enums.pay;

import com.fanglin.enums.CodeEnum;
import lombok.Getter;

/**
 * @author 彭方林
 * @version 1.0
 * @date 2019/7/18 9:42
 **/
public enum TestEnum implements CodeEnum {


    /**
     * 男
     */
    MAN(0),
    /**
     * 女
     */
    WOMAN(1);

    @Getter
    int code;

    TestEnum(int code) {
        this.code = code;
    }
}
