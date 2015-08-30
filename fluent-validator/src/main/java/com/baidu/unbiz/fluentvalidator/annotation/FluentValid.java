package com.baidu.unbiz.fluentvalidator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 与Spring AOP配合，作用于参数用于表示验证。
 * <p/>
 * 作用于属性上，用于表示级联验证。
 *
 * @author zhangxu
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FluentValid {

}
