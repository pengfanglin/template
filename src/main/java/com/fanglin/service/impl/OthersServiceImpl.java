package com.fanglin.service.impl;

import com.fanglin.core.others.Ajax;
import com.fanglin.core.others.BusinessException;
import com.fanglin.mapper.MapperFactory;
import com.fanglin.service.OthersService;
import com.fanglin.utils.OthersUtils;
import com.fanglin.utils.RegexUtils;
import com.fanglin.utils.SmsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 * 其他服务实现类
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:36
 **/
@Service
public class OthersServiceImpl implements OthersService {

    @Autowired
    MapperFactory mapperFactory;
    @Autowired
    JedisPool jedisPool;

    @Override
    public Ajax sendCode(String mobile) {
        if (RegexUtils.checkMobile(mobile)) {
            return Ajax.error("手机号不合法");
        }
        try (Jedis jedis = jedisPool.getResource()) {
            int time = 0;
            while (true) {
                time++;
                String code = OthersUtils.createRandom(4);
                if (jedis.get(mobile) == null) {
                    SmsUtils.zhuTong(mobile, code);
                    jedis.set(String.format("code:%s_%s", mobile, code), "", "ex", 60);
                    return Ajax.ok(code);
                }
                if (time > 100) {
                    return Ajax.error("生成验证码超时，请稍后重试");
                }
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

}
