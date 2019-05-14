package com.fanglin.utils;

import com.fanglin.core.others.Assert;
import com.fanglin.core.token.TokenInfo;

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
public class TokenUtils {

    /**
     * 登录生成令牌
     * @param response
     * @param tokenInfo
     * @param timeout  超时时间 单位/秒
     * @return
     */
    public static TokenInfo login(HttpServletResponse response, TokenInfo tokenInfo, int timeout) {
        Assert.notNull(tokenInfo.getId(),"id不能为空");
        //生成token
        tokenInfo.setToken(UUID.randomUUID().toString());
        //请求头加入token
        response.addHeader("AUTHORIZATION", tokenInfo.getToken());
        //cookie加入token
        Cookie cookie = new Cookie("AUTHORIZATION", tokenInfo.getToken());
        tokenInfo.setTokenTime(new Date());
        if(timeout==-1){
            JedisUtils.set("token:" + tokenInfo.getToken(), JsonUtils.objectToJson(tokenInfo));
        }else{
            cookie.setMaxAge(timeout);
            JedisUtils.set("token:" + tokenInfo.getToken(), JsonUtils.objectToJson(tokenInfo),"ex",timeout);
        }
        response.addCookie(cookie);
        return tokenInfo;
    }
}
