package com.baidu.unbiz.fluentvalidator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.baidu.unbiz.fluentvalidator.Validator;

/**
 * 在类的属性级别加入此注解表示利用注解方式来进行验证
 * <p/>
 * 举例：
 * <pre>
 * public class Car {
 *
 *     @FluentValidate({CarManufacturerValidator.class})
 *     private String manufacturer;
 *
 *     @FluentValidate({CarLicensePlateValidator.class, CarLicensePlateValidator2.class})
 *     private String licensePlate;
 * }
 * </pre>
 *
 * @author zhangxu
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FluentValidate {

    /**
     * 验证器列表，接受{@link Validator}实现类的数组
     */
    Class<? extends Validator>[] value() default {};

}
