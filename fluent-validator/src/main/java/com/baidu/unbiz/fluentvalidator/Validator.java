package com.baidu.unbiz.fluentvalidator;

/**
 * @author zhangxu
 */
public interface Validator<T> {

    boolean accept(ValidatorContext context, T t);

    boolean validate(ValidatorContext context, T t);

    void onException(Throwable throwable, ValidatorContext context, T t);

}
