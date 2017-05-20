package com.baidu.unbiz.fluentvalidator.annotation;

import java.lang.annotation.*;

/**
 * 标记为可为空的注解
 *
 * @author zhangxu
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Nullable {

}
