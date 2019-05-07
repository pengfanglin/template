package com.fanglin.enums.pay;

/**
 * 支付方式
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/10 19:40
 **/
public enum PayWayEnum {
    //小程序
    WX_SMALL_PROGRAM(0,"JSAPI"),
    //微信公众号
    WX_PUB(1,"JSAPI"),
    //微信app
    WX_APP(2,"APP"),
    //微信扫码
    WX_NATIVE(3,"NATIVE"),
    //微信H5
    WX_MWEB(4,"MWEB"),
    //支付宝app
    ALIPAY_APP(5,null);

    /**
     * 支付方式
     */
    private Integer way;
    /**
     * 支付类型
     */
    private String type;

    PayWayEnum(Integer way,String type) {
        this.way=way;
        this.type=type;
    }
    public Integer getWay() {
        return way;
    }

    public String getType() {
        return type;
    }
    public static PayWayEnum findByWay(Integer way) {
        for (PayWayEnum payWayEnum : PayWayEnum.values()) {
            if (payWayEnum.way.equals(way)) {
                return payWayEnum;
            }
        }
        return null;
    }

    public static PayWayEnum findByType(String type) {
        for (PayWayEnum payWayEnum : PayWayEnum.values()) {
            if (payWayEnum.type.equals(type)) {
                return payWayEnum;
            }
        }
        return null;
    }
}