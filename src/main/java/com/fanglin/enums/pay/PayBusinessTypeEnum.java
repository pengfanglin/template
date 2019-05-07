package com.fanglin.enums.pay;

/**
 * 支付业务类型
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/10 19:40
 **/
public enum PayBusinessTypeEnum {
    //测试支付
    TEST(0);

    /**
     * 业务类型
     */
    private Integer value;

    PayBusinessTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static PayBusinessTypeEnum find(Integer value) {
        for (PayBusinessTypeEnum payBusinessTypeEnum : PayBusinessTypeEnum.values()) {
            if (payBusinessTypeEnum.value.equals(value)) {
                return payBusinessTypeEnum;
            }
        }
        return null;
    }
}