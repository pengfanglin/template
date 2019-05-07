package com.fanglin.enums.pay;

/**
 * 支付用户类型（根据creatorType和creatorId唯一确定一条支付记录)
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/10 19:40
 **/
public enum PayCreatorTypeEnum {
    /**
     * 用户
     */
    USER(0),
    /**
     * 公共支付用户对象
     */
    COMMON(1);

    /**
     * 支付方式
     */
    private Integer value;


    PayCreatorTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static PayCreatorTypeEnum find(Integer value) {
        for (PayCreatorTypeEnum payWayEnum : PayCreatorTypeEnum.values()) {
            if (payWayEnum.value.equals(value)) {
                return payWayEnum;
            }
        }
        return null;
    }
}