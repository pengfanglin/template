package com.fanglin.core.pay;


import com.fanglin.enums.pay.PayBusinessTypeEnum;
import com.fanglin.enums.pay.PayCreatorTypeEnum;
import com.fanglin.enums.pay.PayNotifyEnum;
import com.fanglin.enums.pay.PayWayEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 公共退款Bean
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/10 19:54
 **/
@Data
@Accessors(chain = true)
public class CommonRefund {
    /**
     * 支付订单时生成的支付平台流水号，统一采用支付平台流水号进行退款
     */
    private String payTradeNo;
    /**
     * 退款成功后支付平台交易流水号
     */
    private String tradeNo;
    /**
     * 订单金额
     */
    private BigDecimal totalAmount;
    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    /**
     * 退款方式
     */
    private PayWayEnum payWay;
    /**
     * 退款单号，要保证唯一性(自己生成)
     */
    private String refundNo;
    /**
     * 业务类型
     */
    private PayBusinessTypeEnum payBusinessType;
    /**
     * 支付平台额外参数
     */
    private Map<String, Object> metadata;
    /**
     * 退款记录id
     */
    private Long historyId;
    /**
     * 创建者id
     */
    private Long creatorId;
    /**
     * 创建者类型
     */
    private PayCreatorTypeEnum payCreatorType;
    /**
     * 支付记录id
     */
    private Long payId;
    /**
     * 退款原因
     */
    private String refundReason;
    /**
     * 额外退款参数
     */
    private Map<String,Object> extraData;
}
