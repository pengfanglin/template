package com.fanglin.service.impl;

import com.fanglin.common.core.others.Ajax;
import com.fanglin.common.core.others.Assert;
import com.fanglin.common.core.others.BusinessException;
import com.fanglin.common.utils.JedisUtils;
import com.fanglin.common.utils.OthersUtils;
import com.fanglin.common.utils.RegexUtils;
import com.fanglin.common.utils.SmsUtils;
import com.fanglin.enums.others.CodeType;
import com.fanglin.mapper.MapperFactory;
import com.fanglin.service.OthersService;
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

    @Override
    public void sendCode(String mobile, CodeType type) {
        String code = OthersUtils.randomString(4);
        String content = String.format("验证码为:%s,60秒有效", code);
        try (Jedis jedis = JedisUtils.getJedis()) {
            String key = String.format("code:%s:%s", type, mobile);
            code = jedis.get(key);
            if (key != null) {
                long time = jedis.pttl(key);
                Assert.isTrue(time != -2, "验证码未过期，请" + time + "秒后重试");
            }
            Assert.isTrue(SmsUtils.zhuTong(mobile, content), "验证码发送失败");
            jedis.set(key, code, "ex", 60);
        }
    }

}
