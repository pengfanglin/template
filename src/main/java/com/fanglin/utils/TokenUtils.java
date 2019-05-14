package com.fanglin.utils;

import com.fanglin.core.others.Assert;
import com.fanglin.core.token.TokenInfo;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

/**
 * 生成令牌
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/5/11 22:28
 **/
@Component
public class TokenUtils {

    /**
     * 登录生成令牌
     * @param response
     * @param tokenInfo
     * @return
     */
    public static TokenInfo login(HttpServletResponse response, TokenInfo tokenInfo) {
        Assert.notNull(tokenInfo.getId(),"id不能为空");
        Assert.notNull(tokenInfo.getType(),"令牌类型不能为空");
        //生成token
        tokenInfo.setToken(UUID.randomUUID().toString());
        //请求头加入token
        response.addHeader("AUTHORIZATION", tokenInfo.getToken());
        //cookie加入token
        Cookie cookie = new Cookie("AUTHORIZATION", tokenInfo.getToken());
        // 设置为30min
        cookie.setMaxAge(60*60);
        response.addCookie(cookie);
        tokenInfo.setTokenTime(new Date());
        JedisUtils.getJedis().set("token:" + tokenInfo.getToken(), JsonUtils.objectToJson(tokenInfo),"ex",3600);
        return tokenInfo;
    }
}
