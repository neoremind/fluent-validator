package com.baidu.unbiz.fluentvalidator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.baidu.unbiz.fluentvalidator.Validator;

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

    /**
     * 验证器列表，接受{@link Validator}实现类的数组，除了级联外需要处理的额外验证
     */
    Class<? extends Validator>[] value() default {};

}
