package com.fanglin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 移除本地缓存中指定的值
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/16 15:37
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalCacheRemove {
    String value() default "";
}
