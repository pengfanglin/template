package com.fanglin.core.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 本地缓存切面类，首先从缓存中取数据，数据存在返回缓存数据，否则去数据库取
 *
 * @author 方林
 */
@Component
@Aspect()
@Slf4j
public class TestAop {
    /**
     * LocalCache切入点规则
     */
    @Pointcut(value = "@annotation(com.fanglin.annotation.Token)")
    public void pointLocalCache() {

    }

    /**
     * 切入的验证代码
     */
    @Around(value = "pointLocalCache()")
    public Object localCacheAop(ProceedingJoinPoint point) throws Throwable {
        Object result = point.proceed();
        log.info(result.toString());
        return result;
    }

}

