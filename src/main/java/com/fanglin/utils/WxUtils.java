package com.fanglin.utils;


import com.fanglin.core.others.ValidateException;
import com.fanglin.core.others.Wx;
import com.fanglin.properties.WxProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信工具类
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/2 15:46
 **/
@Component
@Slf4j
@ConditionalOnClass(JedisPool.class)
public class WxUtils {
    private static WxProperties wxProperties;
    private static JedisPool jedisPool;
    private final static String JS_API_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
    private final static String ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token";
    private final static String ACCESS_TOKEN_BY_CODE_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
    private final static String USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo";
    private final static String ACCESS_TOKEN_GRANT_TYPE = "client_credential";
    private final static String JS_API_TICKET_TYPE = "jsapi";
    private final static String ACCESS_TOKEN_BY_CODE_GRANT_TYPE = "authorization_code";
    private final static String LANG = "zh_CN";

    public WxUtils(WxProperties wxProperties) {
        WxUtils.wxProperties = wxProperties;
    }

    /**
     * 静态工具类构造注入JedisPool有延时问题，有概率注入失败，所以在每次请求时判断为空时，尝试手动注入
     */
    private static void initJedisPool() {
        if (jedisPool == null) {
            synchronized (WxUtils.class) {
                if (jedisPool == null) {
                    try {
                        WxUtils.jedisPool = SpringUtils.getBean(JedisPool.class);
                    } catch (NoSuchBeanDefinitionException e) {
                        throw new RuntimeException("jedis获取失败，请开启jedis自动配置或者手动配置该对象!");
                    }
                }
            }
        }
    }

    /**
     * 获取微信授权凭证
     *
     * @param accessToken 请求token
     * @return
     */
    public static String getJsApiTicket(String accessToken) {
        initJedisPool();
        Jedis jedis = jedisPool.getResource();
        String jsApiTicket = jedis.get("wxJsApiTicket");
        if (OthersUtils.isEmpty(jsApiTicket)) {
            Map<String, Object> params = new HashMap<>(10);
            params.put("access_token", accessToken);
            params.put("type", JS_API_TICKET_TYPE);
            String result = HttpUtils.get(JS_API_TICKET_URL, params);
            Wx wx = JsonUtils.jsonToObject(result, Wx.class);
            if (wx.getErrcode() == 0) {
                jsApiTicket = wx.getTicket();
                jedis.set("wx_access_token", jsApiTicket, "NX", "EX", 7000);
            } else {
                log.warn("微信jsApiTicket获取失败:{} {}", wx.getErrcode(), wx.getErrmsg());
                throw new ValidateException("微信jsApiTicket获取失败:" + wx.getErrcode() + " " + wx.getErrmsg());
            }
        }
        return jsApiTicket;
    }

    /**
     * 获取公众号的全局唯一接口调用凭据，每个公众号每月有获取次数限制，需要放到redis中，过期后才重新获取
     *
     * @return
     */
    public static String getAccessToken() {
        initJedisPool();
        Jedis jedis = jedisPool.getResource();
        String accessToken = jedis.get("wxAccessToken");
        if (OthersUtils.isEmpty(accessToken)) {
            Map<String, Object> params = new HashMap<>(10);
            params.put("appid", wxProperties.getAppId());
            params.put("secret", wxProperties.getSecret());
            params.put("grant_type", ACCESS_TOKEN_GRANT_TYPE);
            String result = HttpUtils.get(ACCESS_TOKEN, params);
            Wx wx = JsonUtils.jsonToObject(result, Wx.class);
            if (wx.getAccess_token() != null) {
                accessToken = wx.getAccess_token();
                jedis.set("wx_access_token", accessToken, "NX", "EX", 7000);
            } else {
                log.warn("微信jsApiTicket获取失败:{} {}", wx.getErrcode(), wx.getErrmsg());
                throw new ValidateException("微信jsApiTicket获取失败:" + wx.getErrcode() + " " + wx.getErrmsg());
            }
        }
        return accessToken;
    }

    /**
     * 微信网页授权，根据code获取AccessToken
     *
     * @param code 前端生成的请求code
     * @return
     */
    public static Wx getAccessTokenByCode(String code) {
        Map<String, Object> params = new HashMap<>(10);
        params.put("appid", wxProperties.getAppId());
        params.put("secret", wxProperties.getSecret());
        params.put("code", code);
        params.put("grant_type", ACCESS_TOKEN_BY_CODE_GRANT_TYPE);
        String str = HttpUtils.post(ACCESS_TOKEN_BY_CODE_URL, params);
        Wx wxBean = JsonUtils.jsonToObject(str, Wx.class);
        return getUserInfoByOpenId(wxBean.getAccess_token(), wxBean.getOpenid());
    }

    /**
     * 根据openId获取用户信息
     *
     * @param accessToken 授权token
     * @param openId      用户openId
     * @return
     */
    public static Wx getUserInfoByOpenId(String accessToken, String openId) {
        Map<String, Object> params = new HashMap<>(10);
        params.put("access_token", accessToken);
        params.put("openid", openId);
        params.put("lang", LANG);
        String result = HttpUtils.post(USER_INFO_URL, params);
        Wx wx = JsonUtils.jsonToObject(result, Wx.class);
        if (wx.getErrcode() == 0) {
            return wx;
        } else {
            log.warn("用户信息获取失败:{} {}", wx.getErrcode(), wx.getErrmsg());
            throw new ValidateException("用户信息获取失败:" + wx.getErrcode() + " " + wx.getErrmsg());
        }
    }

    /**
     * 微信授权
     *
     * @param url 发起请求的页面地址
     * @return
     */
    public static Wx getWXAuthorization(String url) {
        String accessToken = getAccessToken();
        String jsApiTicket = getJsApiTicket(accessToken);
        String nonceStr = OthersUtils.createRandom(16);
        long timestamp = Long.parseLong(String.valueOf(System.currentTimeMillis()).substring(0, 10));
        Wx wx = new Wx()
            .setTicket(jsApiTicket)
            .setAppId(wxProperties.getAppId())
            .setNonceStr(nonceStr)
            .setTimestamp(timestamp);
        try {
            wx.setUrl(URLDecoder.decode(url, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.warn("url解码失败:{}", e.getMessage());
            throw new ValidateException("url解码失败:" + e.getMessage());
        }
        String sign = "jsapi_ticket=" + jsApiTicket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;
        wx.setSignature(EncodeUtils.sha1(sign));
        return wx;
    }
}
