package com.fanglin.service;


import com.fanglin.core.pay.CommonRefund;

import java.util.Map;

/**
 * 支付业务处理
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/11 18:22
 **/
public interface PayService {
    /**
     * 支付宝支付通知
     *
     * @param params
     * @return
     */
    boolean alipayPayNotify(Map<String, String> params);

    /**
     * 微信支付通知
     *
     * @param params
     * @return
     */
    Map<String, Object> wxPayNotify(Map<String, Object> params);

    /**
     * 微信退款通知
     *
     * @param params
     * @return
     */
    Map<String, Object> wxRefundNotify(Map<String, Object> params);

    /**
     * 退款成功处理器
     *
     * @param commonRefund
     * @return
     */
    boolean refundSuccessHandler(CommonRefund commonRefund);
}
