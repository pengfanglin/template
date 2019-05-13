package com.fanglin.core.aop;

import com.fanglin.annotation.LocalCache;
import com.fanglin.annotation.LocalCacheRemove;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地缓存切面类，首先从缓存中取数据，数据存在返回缓存数据，否则去数据库取
 *
 * @author 方林
 */
@Component
@Aspect()
public class LocalCacheAop {
    /**
     * 本地缓存仓库
     */
    private static Map<String, CacheData> cache = new ConcurrentHashMap<>();

    /**
     * LocalCache切入点规则
     */
    @Pointcut(value = "@annotation(com.fanglin.annotation.LocalCache)")
    public void pointLocalCache() {

    }

    /**
     * LocalCacheRemove切入点规则
     */
    @Pointcut(value = "@annotation(com.fanglin.annotation.LocalCacheRemove)")
    public void pointLocalCacheRemove() {

    }

    /**
     * 切入的验证代码
     */
    @Around(value = "pointLocalCache()")
    public Object localCacheAop(ProceedingJoinPoint point) throws Throwable {
        MethodSignature joinPointObject = (MethodSignature) point.getSignature();
        Method method = joinPointObject.getMethod();
        LocalCache localCache = method.getAnnotation(LocalCache.class);
        long timeout = localCache.timeout();
        String key = getCacheKey(method, point.getArgs(), localCache.value());
        CacheData cacheData = cache.get(key);
        //本地缓存为空时或者用户设置了超时时间并且已经超时，需要重新加载数据
        boolean reload = cacheData == null || (cacheData.getOverdueTime() != -1 && cacheData.getOverdueTime() < System.currentTimeMillis());
        if (reload) {
            Object result = point.proceed();
            if (result != null || localCache.cacheNull()) {
                if(timeout!=-1){
                    switch (localCache.unit()){
                        case DAYS:
                            timeout=timeout*24*3600*1000;
                            break;
                        case HOURS:
                            timeout=timeout*3600*1000;
                            break;
                        case MINUTES:
                            timeout=timeout*60*1000;
                            break;
                        case SECONDS:
                            timeout=timeout*1000;
                            break;
                        case MILLISECONDS:
                            break;
                        default:
                            throw new RuntimeException("不支持的时间单位");

                    }
                }
                cacheData = new CacheData(result, timeout == -1 ? -1 : System.currentTimeMillis() + timeout);
                cache.put(key, cacheData);
            }
        }
        return cacheData == null ? null : cacheData.getData();
    }

    /**
     * 切入的验证代码
     */
    @AfterReturning(value = "pointLocalCacheRemove()")
    public void localCacheRemoveAop(JoinPoint point) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        LocalCacheRemove localCacheRemove = method.getAnnotation(LocalCacheRemove.class);
        String key = getCacheKey(method, point.getArgs(), localCacheRemove.value());
        cache.remove(key);
    }

    /**
     * 解析el表达式生成缓存的key
     *
     * @param args         目标方法参数
     * @param key          表达式
     * @return
     */
    private String getCacheKey(Method method, Object[] args, String key) {
        //创建SpringEL表达式转换器
        ExpressionParser parser = new SpelExpressionParser();
        //Spring
        EvaluationContext context = new StandardEvaluationContext();
        //获取目标方法参数名
        String[] paramNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(method);
        String defaultKey = method.getDeclaringClass().getName() + ":" + method.getName();
        if (paramNames == null) {
            return defaultKey;
        }
        for (int i = 0; i < args.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        try {
            Expression expression = parser.parseExpression(key);
            Object value = expression.getValue(context);
            return value == null || "".equals(value) ? defaultKey : value.toString();
        } catch (Exception e) {
            return defaultKey;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class CacheData {
        private Object data;
        private long overdueTime;
    }
}

