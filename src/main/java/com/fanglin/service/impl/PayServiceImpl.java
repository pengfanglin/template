package com.fanglin.service.impl;


import com.fanglin.core.others.Assert;
import com.fanglin.core.pay.CommonPay;
import com.fanglin.core.pay.CommonRefund;
import com.fanglin.entity.pay.PayHistoryEntity;
import com.fanglin.entity.pay.RefundHistoryEntity;
import com.fanglin.enums.pay.PayBusinessTypeEnum;
import com.fanglin.enums.pay.PayWayEnum;
import com.fanglin.core.others.ValidateException;
import com.fanglin.mapper.MapperFactory;
import com.fanglin.service.PayService;
import com.fanglin.utils.EncodeUtils;
import com.fanglin.utils.OthersUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付服务
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/24 19:18
 **/
@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Autowired
    MapperFactory mapperFactory;

    /**
     * 支付成功后的业务处理
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean paySuccessHandler(CommonPay commonPay) {
        if (commonPay.getHistoryId() == null) {
            throw new ValidateException("支付记录id不能为空");
        }
        PayHistoryEntity payHistory = mapperFactory.payHistoryMapper.selectByPrimaryKey(commonPay.getHistoryId());
        Assert.notNull(payHistory, "支付记录不存在");
        if (payHistory.getPayAmount().compareTo(commonPay.getPayAmount()) != 0) {
            throw new ValidateException(String.format("支付金额有误,预期支付:%s,实际支付:%s", payHistory.getPayAmount().toString(), commonPay.getPayAmount().toString()));
        }
        //支付记录设为已支付
        mapperFactory.payHistoryMapper.updateByPrimaryKeySelective(new PayHistoryEntity().setHistoryId(payHistory.getHistoryId()).setState(1).setTradeNo(commonPay.getTradeNo()));
        PayBusinessTypeEnum payBusinessType = PayBusinessTypeEnum.find(payHistory.getPayBusinessType());
        Assert.notNull(payBusinessType, "业务类型不存在");
        switch (payBusinessType) {
            case TEST:
                return true;
            default:
                throw new ValidateException("业务逻辑未实现");
        }
    }

    /**
     * 退款成功后的业务处理
     */
    @Override
    public boolean refundSuccessHandler(CommonRefund commonRefund) {
        RefundHistoryEntity refundHistory = mapperFactory.refundHistoryMapper.selectOne(new RefundHistoryEntity().setOrderNo(commonRefund.getRefundNo()));
        Assert.notNull(refundHistory, "退款记录不存在");
        if (refundHistory.getRefundAmount().compareTo(commonRefund.getRefundAmount()) != 0) {
            throw new ValidateException(String.format("退款金额有误,预期退款:%s,实际退款:%s", refundHistory.getRefundAmount().toString(), commonRefund.getRefundAmount().toString()));
        }
        //支付记录设为已支付
        mapperFactory.refundHistoryMapper.updateByPrimaryKeySelective(
            new RefundHistoryEntity()
                .setHistoryId(commonRefund.getHistoryId())
                .setState(1)
                .setTradeNo(commonRefund.getTradeNo())
        );
        mapperFactory.payHistoryMapper.addRefundAmount(refundHistory.getPayId(), commonRefund.getRefundAmount().setScale(2, RoundingMode.DOWN).toString());
        PayBusinessTypeEnum payBusinessType = PayBusinessTypeEnum.find(refundHistory.getPayBusinessType());
        Assert.notNull(payBusinessType, "业务类型不存在");
        switch (payBusinessType) {
            case TEST:
                return true;
            default:
                throw new ValidateException("业务逻辑未实现");
        }
    }

    /**
     * 支付宝支付通知处理
     */
    @Override
    public boolean alipayPayNotify(Map<String, String> params) {
        //支付成功
        if ("TRADE_SUCCESS".equals(params.get("trade_status"))) {
            CommonPay commonPay = new CommonPay();
            //生成支付的时候，后端生成的订单号
            commonPay
                //支付方式
                .setPayWay(PayWayEnum.ALIPAY_APP)
                //支付宝生成的交易流水号
                .setTradeNo(params.get("trade_no"))
                //支付金额
                .setPayAmount(new BigDecimal(params.get("total_amount")));
            commonPay.setHistoryId(Long.valueOf(params.get("passback_params")));
            //进行业务处理
            return this.paySuccessHandler(commonPay);
        } else {
            return false;
        }
    }

    /**
     * 微信支付通知处理
     */
    @Override
    public Map<String, Object> wxPayNotify(Map<String, Object> params) {
        //app端调起支付成功
        if ("SUCCESS".equals(params.get("return_code"))) {
            //支付成功
            if ("SUCCESS".equals(params.get("result_code"))) {
                CommonPay commonPay = new CommonPay();
                commonPay
                    //微信生成的交易流水号
                    .setTradeNo(params.get("transaction_id").toString())
                    //支付金额
                    .setPayAmount(new BigDecimal(params.get("total_fee").toString()).divide(new BigDecimal(100), 2, RoundingMode.DOWN));
                //发起支付的时候传递给微信的额外参数
                if (!OthersUtils.isEmpty(params.get("attach"))) {
                    commonPay.setHistoryId(Long.valueOf(params.get("attach").toString()));
                }
                //如果业务处理成功，向微信发送确认消息
                if (this.paySuccessHandler(commonPay)) {
                    log.info("订单号【" + params.get("out_trade_no") + "】支付成功，响应微信请求");
                    Map<String, Object> result = new HashMap<>(2);
                    result.put("return_code", "SUCCESS");
                    result.put("return_msg", "OK");
                    return result;
                }
            } else {
                //支付失败，打印错误信息
                String sb = "错误码:" + params.get("err_code") +
                    "错误代码描述:" + params.get("err_code_des") +
                    "交易类型:" + params.get("trade_type") +
                    "付款银行:" + params.get("bank_type") +
                    "支付金额:" + params.get("total_fee") +
                    "微信支付订单号:" + params.get("transaction_id") +
                    "商户订单号:" + params.get("out_trade_no") +
                    "支付完成时间:" + params.get("time_end");
                log.warn(sb);
            }
        } else {
            //app端调起支付失败，打印错误信息
            log.warn(params.get("return_msg").toString());
        }
        return null;
    }

    /**
     * 微信退款通知处理
     */
    @Override
    public Map<String, Object> wxRefundNotify(Map<String, Object> params) {
        //发起退款成功
        if ("SUCCESS".equals(params.get("return_code"))) {
            //特殊字段经过加密，需要进行解密
            String reqInfo = params.get("reqInfo").toString();
            reqInfo = EncodeUtils.aesDecode(reqInfo, params.get("merchants_key").toString());
            Map<String, Object> data = OthersUtils.xmlToMap(reqInfo);
            CommonRefund commonRefund = new CommonRefund();
            commonRefund
                //微信生成的交易流水号
                .setTradeNo(data.get("transaction_id").toString())
                //商户退款单号
                .setRefundNo(data.get("out_refund_no").toString())
                //退款金额
                .setRefundAmount(new BigDecimal(data.get("settlement_refund_fee").toString()).divide(new BigDecimal(100), 2, RoundingMode.DOWN))
                //订单总金额
                .setTotalAmount(new BigDecimal(data.get("total_fee").toString()).divide(new BigDecimal(100), 2, RoundingMode.DOWN));
            //退款成功
            if ("SUCCESS".equals(data.get("refund_status"))) {
                //如果业务处理成功，向微信发送确认消息
                if (this.refundSuccessHandler(commonRefund)) {
                    log.info("订单号【" + data.get("out_trade_no") + "】支付成功，响应微信请求");
                    Map<String, Object> result = new HashMap<>(2);
                    result.put("return_code", "SUCCESS");
                    result.put("return_msg", "OK");
                    return result;
                }
            } else {
                //退款失败，打印错误信息
                String sb =
                    "退款金额:" + data.get("settlement_refund_fee") +
                        "微信支付订单号:" + data.get("transaction_id") +
                        "商户订单号:" + data.get("out_trade_no") +
                        "退款完成时间:" + params.get("success_time");
                log.warn(sb);
            }
        } else {
            //发起支付失败，打印错误信息
            log.warn(params.get("return_msg").toString());
        }
        return null;
    }

}
