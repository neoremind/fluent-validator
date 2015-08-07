package com.baidu.unbiz.fluentvalidator.jsr303;

import java.util.Set;

import javax.validation.ConstraintViolation;

import com.baidu.unbiz.fluentvalidator.NotThreadSafe;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;

/**
 * @author zhangxu
 */
@NotThreadSafe
public class HibernateSupportedValidator<T> extends ValidatorHandler<T> implements Validator<T> {

    /**
     * A Validator instance is thread-safe and may be reused multiple times. It thus can safely be stored in a static
     * field and be used in the test methods to validate the different Car instances.
     */
    private static javax.validation.Validator validator;

    @Override
    public boolean accept(ValidatorContext context, T t) {
        return true;
    }

    @Override
    public boolean validate(ValidatorContext context, T t) {
        Set<ConstraintViolation<T>> constraintViolations =
                validator.validate(t);
        if (constraintViolations.isEmpty()) {
            return true;
        } else {
            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                context.addErrorMsg("{" + constraintViolation.getPropertyPath() + "} " +
                        constraintViolation.getMessage());
            }
            return false;
        }
    }

    @Override
    public void onException(Exception e, ValidatorContext context, T t) {

    }

    public javax.validation.Validator getValidator() {
        return validator;
    }

    public HibernateSupportedValidator<T> setValidator(javax.validation.Validator validator) {
        HibernateSupportedValidator.validator = validator;
        return this;
    }
}