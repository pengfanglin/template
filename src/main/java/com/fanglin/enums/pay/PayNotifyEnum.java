package com.fanglin.enums.pay;

/**
 * 支付通知
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/10 19:40
 **/
public enum PayNotifyEnum {
    //小程序
    WX_PAY("http://template.qubaotang.cn/pay/wxPayNotify"),
    WX_REFUND("http://template.qubaotang.cn/pay/wxRefundNotify"),
    ALIPAY_PAY("https://template.qubaotang.cn/pay/alipayPayNotify");

    private String value;

    PayNotifyEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }}