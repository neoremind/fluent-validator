package com.baidu.unbiz.fluentvalidator;

/**
 * @author zhangxu
 */
@ThreadSafe
public class ValidatorHandler<T> implements Validator<T> {

    @Override
    public boolean accept(ValidatorContext context, T t) {
        return true;
    }

    @Override
    public boolean validate(ValidatorContext context, T t) {
        return true;
    }

    @Override
    public void onException(Throwable throwable, ValidatorContext context, T t) {

    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
