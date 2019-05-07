package com.fanglin.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;

import com.fanglin.core.others.Assert;
import com.fanglin.core.others.ValidateException;
import com.fanglin.core.pay.CommonPay;
import com.fanglin.core.pay.CommonRefund;
import com.fanglin.entity.pay.PayHistoryEntity;
import com.fanglin.entity.pay.RefundHistoryEntity;
import com.fanglin.enums.pay.PayNotifyEnum;
import com.fanglin.enums.pay.PayWayEnum;
import com.fanglin.mapper.MapperFactory;
import com.fanglin.properties.PayProperties;
import com.fanglin.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.*;

/**
 * 支付工具类
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/11 13:37
 **/
@Component
@Slf4j
@ConditionalOnClass(AlipayClient.class)
public class PayUtils {

    /**
     * 支付参数
     */
    private static PayProperties payProperties;
    /**
     * 支付宝请求客户端
     */
    private static AlipayClient alipayClient;
    /**
     * 数据库操作mapper工厂
     */
    private static MapperFactory mapperFactory;
    /**
     * 支付业务处理
     */
    private static PayService payService;

    public PayUtils(PayProperties payProperties, @Autowired(required = false) AlipayClient alipayClient, MapperFactory mapperFactory, PayService payService) {
        PayUtils.payProperties = payProperties;
        PayUtils.alipayClient = alipayClient;
        PayUtils.mapperFactory = mapperFactory;
        PayUtils.payService=payService;
    }

    /**
     * 根据支付类型，调用不同的支付接口
     *
     * @param commonPay 公共支付对象
     * @return
     */
    public static Map<String, Object> pay(CommonPay commonPay) {
        Assert.notNull(commonPay.getPayWay(), "支付类型不能为空!");
        //设置支付类型
        commonPay.setTradeType(commonPay.getPayWay().getType());
        Assert.notNull(commonPay.getPayAmount(), "支付金额不能为空!");
        Assert.notEmpty(commonPay.getOrderNo(), "订单号不能为空!");
        Assert.notNull(commonPay.getCreatorId(), "支付发起者id不能为空");
        Assert.notNull(commonPay.getPayCreatorType(), "支付发起者类型不能为空");
        Assert.notNull(commonPay.getPayBusinessType(), "支付业务类型不能为空");
        if (OthersUtils.isEmpty(commonPay.getOrderName())) {
            commonPay.setOrderName("趣宝堂");
        }
        if (OthersUtils.isEmpty(commonPay.getBody())) {
            commonPay.setBody(commonPay.getOrderName());
        }
        insertPayHistory(commonPay);
        Map<String, Object> result;
        switch (commonPay.getPayWay()) {
            case WX_SMALL_PROGRAM:
            case WX_PUB:
            case WX_APP:
            case WX_NATIVE:
                result = wxPay(commonPay);
                break;
            case ALIPAY_APP:
                result = alipayPay(commonPay);
                break;
            default:
                throw new RuntimeException("支付方式处理逻辑未定义");
        }
        return result;
    }

    /**
     * 添加本地支付记录
     *
     * @param commonPay
     */
    private static void insertPayHistory(CommonPay commonPay) {
        PayHistoryEntity payHistory = new PayHistoryEntity()
            .setPayAmount(commonPay.getPayAmount())
            .setPayBusinessType(commonPay.getPayBusinessType().getValue())
            .setPayWay(commonPay.getPayWay().getWay())
            .setCreatorId(commonPay.getCreatorId())
            .setCreatorType(commonPay.getPayCreatorType().getValue())
            .setOrderNo(commonPay.getOrderNo())
            .setTradeType(commonPay.getPayWay().getType());
        if (commonPay.getExtraData() != null) {
            payHistory.setExtraData(JsonUtils.objectToJson(commonPay.getExtraData()));
        }
        mapperFactory.payHistoryMapper.insertSelective(payHistory);
        commonPay.setHistoryId(payHistory.getHistoryId());
    }
    /**
     * 添加本地支付记录
     *
     * @param commonPay
     */
    private static void insertRefundHistory(CommonRefund commonRefund) {
        RefundHistoryEntity refundHistory = new RefundHistoryEntity()
            .setRefundAmount(commonRefund.getRefundAmount())
            .setPayBusinessType(commonRefund.getPayBusinessType().getValue())
            .setCreatorId(commonRefund.getCreatorId())
            .setCreatorType(commonRefund.getPayCreatorType().getValue())
            .setOrderNo(commonRefund.getRefundNo())
            .setPayId(commonRefund.getPayId());
        if (commonRefund.getExtraData() != null) {
            refundHistory.setExtraData(JsonUtils.objectToJson(commonRefund.getExtraData()));
        }
        mapperFactory.refundHistoryMapper.insertSelective(refundHistory);
        commonRefund.setHistoryId(refundHistory.getHistoryId());
    }
    /**
     * 根据退款类型，调用不同的退款接口
     *
     * @param commonRefund 公共退款对象
     * @return
     */
    public static boolean refund(CommonRefund commonRefund) {
        Assert.notNull(commonRefund.getPayId(), "支付记录id不能为空");
        Assert.notNull(commonRefund.getCreatorId(), "支付发起者id不能为空");
        Assert.notNull(commonRefund.getPayCreatorType(), "支付发起者类型不能为空");
        //交易流水号
        Assert.notEmpty(commonRefund.getTradeNo(), "交易流水号不能为空");
        //退款单号
        Assert.notEmpty(commonRefund.getRefundNo(), "退款单号不能为空");
        Assert.notNull(commonRefund.getRefundAmount(), "退款金额不能为空");
        //退款金额(单位分)
        if (commonRefund.getTotalAmount().compareTo(new BigDecimal(0)) <= 0) {
            throw new ValidateException("退款金额必须大于0");
        }
        insertRefundHistory(commonRefund);
        switch (commonRefund.getPayWay()) {
            case WX_SMALL_PROGRAM:
            case WX_PUB:
            case WX_APP:
                return wxRefund(commonRefund);
            case ALIPAY_APP:
                return alipayRefund(commonRefund);
            default:
                throw new RuntimeException("支付方式不存在");
        }
    }

    /**
     * 微信支付参数校验
     *
     * @param commonPay 公共支付对象
     * @return
     */
    private static Map<String, Object> wxPayValidate(CommonPay commonPay) {
        //请求参数
        Map<String, Object> params = new LinkedHashMap<>();
        //微信移动应用appid
        params.put("appid", payProperties.getWx().getAppId());
        //商户号
        params.put("mch_id", payProperties.getWx().getMchId());
        switch (commonPay.getPayWay()) {
            case WX_SMALL_PROGRAM:
            case WX_PUB:
                Assert.notEmpty(commonPay.getOpenid(), "JSAPI支付必须openid");
                //微信公众号openid
                params.put("openid", commonPay.getOpenid());
                break;
            default:
                break;
        }
        //支付金额(单位分)
        if (commonPay.getPayAmount().compareTo(new BigDecimal(0)) <= 0) {
            throw new ValidateException("支付金额必须大于0");
        }
        params.put("body", commonPay.getBody());
        params.put("total_fee", commonPay.getPayAmount().multiply(new BigDecimal(100)).setScale(0, RoundingMode.DOWN).intValue());
        params.put("out_trade_no", commonPay.getOrderNo());
        params.put("nonce_str", commonPay.getOrderNo());
        //支付终端ip
        params.put("spbill_create_ip", "47.101.151.125");
        //回调通知地址
        params.put("notify_url", PayNotifyEnum.WX_PAY.getValue());
        params.put("trade_type", commonPay.getTradeType());
        params.put("attach", commonPay.getHistoryId());
        return wxSign(params);
    }

    /**
     * 对参数进行微信签名
     *
     * @param params 需要签名的参数
     * @return
     */
    private static Map<String, Object> wxSign(Map<String, Object> params) {
        //对参数进行字典序排序(从小到大)
        List<String> keys = new ArrayList<>(params.keySet());
        //对key键值按字典升序排序
        Collections.sort(keys);
        Map<String, Object> sortParams = new LinkedHashMap<>(10);
        for (String key : keys) {
            sortParams.put(key, params.get(key));
        }
        //拼接需要签名的字符串
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : sortParams.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        //拼接商户的api_key
        sb.append("key=").append(payProperties.getWx().getSecret());
        sortParams.put("sign", EncodeUtils.md5Encode(sb.toString()).toUpperCase());
        return sortParams;
    }

    /**
     * 构建返回结果，并二次签名
     *
     * @param params
     * @param key    app秘钥
     * @return
     */
    private static Map<String, Object> buildWxPayResult(Map<String, Object> params) {
        if ("SUCCESS".equals(params.get("return_code"))) {
            if ("SUCCESS".equals(params.get("result_code"))) {
                //对微信返回数据进行二次签名
                Map<String, Object> signParams = new LinkedHashMap<>(10);
                if (PayWayEnum.WX_PUB.getType().equals(params.get("trade_type"))) {
                    signParams.put("appId", params.get("appid"));
                    signParams.put("package", "prepay_id=" + params.get("prepay_id"));
                    signParams.put("nonceStr", params.get("nonce_str"));
                    signParams.put("timeStamp", (System.currentTimeMillis() / 1000) + "");
                    signParams.put("signType", "MD5");
                } else if (PayWayEnum.WX_APP.getType().equals(params.get("trade_type"))) {
                    signParams.put("appid", params.get("appid"));
                    signParams.put("partnerid", params.get("mch_id"));
                    signParams.put("prepayid", params.get("prepay_id"));
                    signParams.put("noncestr", params.get("nonce_str"));
                    signParams.put("timestamp", (System.currentTimeMillis() / 1000) + "");
                    signParams.put("package", "Sign=WXPay");
                } else {
                    signParams.put("prepay_id", params.get("prepay_id"));
                    signParams.put("code_url", params.get("code_url"));
                }
                if (PayWayEnum.WX_PUB.getType().equals(params.get("trade_type"))) {
                    signParams.put("paySign", wxSign(signParams).get("sign"));
                } else if (PayWayEnum.WX_APP.getType().equals(params.get("trade_type"))) {
                    signParams.put("sign", wxSign(signParams).get("sign"));
                }
                return signParams;
            } else {
                log.warn("微信支付请求失败:{} ", params.get("err_code_des"));
                throw new ValidateException("微信支付请求失败:" + params.get("err_code_des").toString());
            }
        } else {
            log.warn("支付失败:{} ", params.get("return_msg"));
            throw new ValidateException("支付失败:" + params.get("return_msg").toString());
        }
    }

    /**
     * 微信支付
     */
    private static Map<String, Object> wxPay(CommonPay commonPay) {
        Map<String, Object> sortParams = wxPayValidate(commonPay);
        String result = HttpUtils.wxPostByXml("https://api.mch.weixin.qq.com/pay/unifiedorder", sortParams);
        Map<String, Object> returnParams = OthersUtils.xmlToMap(result);
        return buildWxPayResult(returnParams);
    }

    /**
     * 微信退款
     */
    public static boolean wxRefund(CommonRefund commonRefund) {
        //请求参数
        Map<String, Object> params = new LinkedHashMap<>();
        //微信移动应用appid
        params.put("appid", payProperties.getWx().getAppId());
        //商户号
        params.put("mch_id", payProperties.getWx().getMchId());
        wxRefundValidate(commonRefund, params);
        String result = HttpUtils.wxPostByXml("https://api.mch.weixin.qq.com/secapi/pay/refund", wxSign(params));
        Map<String, Object> returnParam = OthersUtils.xmlToMap(result);
        if ("SUCCESS".equals(returnParam.get("return_code"))) {
            if ("SUCCESS".equals(returnParam.get("result_code"))) {
                return true;
            } else {
                log.warn("微信退款请求失败: {}" ,returnParam.get("err_code_des"));
                throw new ValidateException("微信退款请求失败【" + returnParam.get("err_code_des") + "】");
            }
        } else {
            log.warn("连接微信退款服务失败: {}" ,returnParam.get("return_msg"));
            throw new ValidateException("连接微信退款服务失败【" + returnParam.get("return_msg") + "】");
        }
    }

    /**
     * 微信退款参数校验
     *
     * @param commonRefund
     * @param params
     */
    private static void wxRefundValidate(CommonRefund commonRefund, Map<String, Object> params) {
        params.put("transaction_id", commonRefund.getTradeNo());
        //随机字符串
        params.put("nonce_str", commonRefund.getHistoryId());
        params.put("out_refund_no", commonRefund.getRefundNo());
        Assert.notNull(commonRefund.getTotalAmount(), "订单总金额不能为空");
        //订单总金额(单位分)
        if (commonRefund.getTotalAmount().compareTo(new BigDecimal(0)) <= 0) {
            throw new ValidateException("订单总金额必须大于0");
        }
        params.put("total_fee", commonRefund.getTotalAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN).intValue());
        params.put("refund_fee", commonRefund.getRefundAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN).intValue());
        if (commonRefund.getTotalAmount().compareTo(commonRefund.getRefundAmount()) <= 0) {
            throw new ValidateException("退款金额超出订单金额");
        }
        params.put("notify_url", PayNotifyEnum.WX_REFUND.getValue());
    }

    /**
     * 支付宝支付
     */
    public static Map<String, Object> alipayPay(CommonPay commonPay) {
        // 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest payRequest = new AlipayTradeAppPayRequest();
        // SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        //对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body
        model.setBody(commonPay.getBody());
        // 商品的标题/交易标题/订单标题/订单关键字等
        model.setSubject(commonPay.getOrderName());
        // 商户订单号(自动生成)
        model.setOutTradeNo(commonPay.getOrderNo());
        // 支付金额
        model.setTotalAmount(commonPay.getPayAmount().toString());
        //额外数据
        model.setPassbackParams(commonPay.getHistoryId().toString());
        //销售产品码，商家和支付宝签约的产品码，为固定值
        model.setProductCode("UICK_MSECURITY_PAY");
        //支付超时时间
        model.setTimeoutExpress("15h");
        //支付参数
        payRequest.setBizModel(model);
        // 回调地址
        payRequest.setNotifyUrl(PayNotifyEnum.ALIPAY_PAY.getValue());
        AlipayTradeAppPayResponse response;
        try {
            //发起支付
            response = alipayClient.sdkExecute(payRequest);
        } catch (AlipayApiException e) {
            log.warn("连接支付宝服务失败:{}", e.getMessage());
            throw new ValidateException("连接支付宝服务失败【" + e.getMessage() + "】");
        }
        //发起支付成功，将生成的支付凭证返给app
        if (response.isSuccess()) {
            Map<String, Object> result = new HashMap<>(10);
            result.put("body", response.getBody());
            return result;
        } else {
            log.warn("生成支付凭据失败:{}", response.getMsg());
            throw new ValidateException("生成支付凭据失败【" + response.getMsg() + "】");
        }
    }

    /**
     * 支付宝退款
     */
    public static boolean alipayRefund(CommonRefund commonRefund) {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        //支付成功后支付宝返回的交易流水号
        model.setTradeNo(commonRefund.getPayTradeNo());
        //退款金额
        model.setRefundAmount(commonRefund.getRefundAmount().setScale(2, RoundingMode.DOWN).toString());
        //退款单号要保证唯一性
        model.setOutRequestNo(commonRefund.getRefundNo());
        //退款原因
        if(commonRefund.getRefundReason()!=null){
            model.setRefundReason(commonRefund.getRefundReason());
        }
        //设置退款参数
        request.setBizModel(model);
        //发起退款
        AlipayTradeRefundResponse response;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            log.warn("发起退款异常:{}",e.getErrMsg());
            throw new ValidateException("发起退款异常");
        }
        //发起退款成功
        if (response.isSuccess()) {
            //退款成功
            if ("10000".equals(response.getCode())) {
                //退款成功，此处处理业务逻辑
                commonRefund.setTradeNo(response.getTradeNo());
                commonRefund.setRefundAmount(new BigDecimal(response.getRefundFee()));
                payService.refundSuccessHandler(commonRefund);
            } else if ("40004".equals(response.getCode())) {
                throw new ValidateException("退款金额大于支付金额");
            } else {
                log.warn(response.getSubMsg());
                throw new ValidateException(response.getSubMsg());
            }
        } else {
            log.warn("发起退款失败:{}",response.getMsg());
            throw new ValidateException("发起退款失败【" + response.getMsg() + "】");
        }
        return false;
    }
}
