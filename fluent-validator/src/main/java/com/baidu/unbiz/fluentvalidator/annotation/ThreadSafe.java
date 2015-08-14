package com.baidu.unbiz.fluentvalidator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记为线程安全的注解，标示类或者方法必须是线程安全实现的
 *
 * @author zhangxu
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ThreadSafe {

}
