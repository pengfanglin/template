package com.fanglin.core.pay;

import com.fanglin.enums.pay.PayBusinessTypeEnum;
import com.fanglin.enums.pay.PayCreatorTypeEnum;
import com.fanglin.enums.pay.PayWayEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 公共支付Bean
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/24 15:10
 **/
@Data
@Accessors(chain = true)
public class CommonPay {
    /**
     * 商户订单号(自己生成)
     */
    private String orderNo;
    /**
     * 支付平台交易流水号
     */
    private String tradeNo;
    /**
     * 用户支付时显示的支付名称
     */
    private String orderName;
    /**
     * 支付金额
     */
    private BigDecimal payAmount;
    /**
     * 支付方式(用户自定义)
     */
    private PayWayEnum payWay;
    /**
     * 支付类型（平台定义）
     */
    private String tradeType;
    /**
     * 详细介绍信息
     */
    private String body;
    /**
     * 业务类型
     */
    private PayBusinessTypeEnum payBusinessType;
    /**
     * 微信openid,仅限公众号支付
     */
    private String openid;
    /**
     * 支付平台额外参数
     */
    private Map<String, Object> metadata;
    /**
     * 本地支付记录id
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
     * 额外数据
     */
    private Map<String,Object> extraData;
}
