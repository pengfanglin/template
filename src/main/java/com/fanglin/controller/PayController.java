package com.fanglin.controller;

import com.fanglin.core.others.Ajax;
import com.fanglin.core.pay.CommonPay;
import com.fanglin.enums.pay.PayBusinessTypeEnum;
import com.fanglin.enums.pay.PayCreatorTypeEnum;
import com.fanglin.enums.pay.PayWayEnum;
import com.fanglin.properties.PayProperties;
import com.fanglin.service.PayService;
import com.fanglin.utils.LogUtils;
import com.fanglin.utils.OthersUtils;
import com.fanglin.utils.PayUtils;
import com.fanglin.utils.UUIDUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 支付
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/11 18:20
 **/
@RestController
@RequestMapping("/pay/")
@Api(value = "/pay/", tags = {"支付"})
public class PayController {

    @Autowired
    PayService payService;

    @ApiOperation("支付测试")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "openId", value = "支付的openId")
    })
    @PostMapping("pay")
    public Ajax pay(String openId) {
        CommonPay commonPay = new CommonPay()
            .setPayAmount(new BigDecimal(0.01))
            .setPayWay(PayWayEnum.WX_SMALL_PROGRAM)
            .setPayBusinessType(PayBusinessTypeEnum.TEST)
            .setBody("测试支付")
            .setOrderName("测试支付")
            .setOpenid(OthersUtils.isEmpty(openId) ? "oGDHy0NDoGmgMHyIt3PlHvOxujkk" : openId)
            .setCreatorId(1L)
            .setPayCreatorType(PayCreatorTypeEnum.USER)
            .setOrderNo(String.valueOf(UUIDUtils.nextId()));
        return Ajax.ok(PayUtils.pay(commonPay));
    }

    @ApiOperation("支付宝支付成功回调")
    @PostMapping("alipayPayNotify")
    public String alipayPayNotify(@RequestParam Map<String, String> params) {
        if (payService.alipayPayNotify(params)) {
            return "success";
        } else {
            return "";
        }
    }

    @ApiOperation("微信支付成功回调")
    @PostMapping("wxPayNotify")
    public String wxPayNotify(HttpServletRequest request) {
        Map<String, Object> params = OthersUtils.xmlToMap(OthersUtils.readDataFromRequest(request));
        //如果业务处理成功，则向微信发送确认信息
        Map<String, Object> result = payService.wxPayNotify(params);
        if (result != null) {
            return OthersUtils.mapToXml(result);
        } else {
            return "";
        }
    }

    @ApiOperation("微信订单退款回调")
    @PostMapping("wxRefundNotify")
    public String wxRefundNotify(HttpServletRequest request) {
        Map<String, Object> params = OthersUtils.xmlToMap(OthersUtils.readDataFromRequest(request));
        //如果业务处理成功，则向微信发送确认信息
        Map<String, Object> result = payService.wxRefundNotify(params);
        if (result != null) {
            return OthersUtils.mapToXml(result);
        } else {
            return "";
        }
    }

}
