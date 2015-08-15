package com.baidu.unbiz.fluentvalidator.jsr303;

import java.util.Set;

import javax.validation.ConstraintViolation;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.annotation.NotThreadSafe;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.baidu.unbiz.fluentvalidator.util.CollectionUtil;

/**
 * 定制JSR303的实现<tt>Hibernate Validator</tt>验证器
 *
 * @author zhangxu
 */
@NotThreadSafe
public class HibernateSupportedValidator<T> extends ValidatorHandler<T> implements Validator<T> {

    /**
     * A Validator instance is thread-safe and may be reused multiple times. It thus can safely be stored in a static
     * field and be used in the test methods to validate the different Car instances.
     */
    private static javax.validation.Validator HIBERANTE_VALIDATOR;

    @Override
    public boolean accept(ValidatorContext context, T t) {
        return true;
    }

    @Override
    public boolean validate(ValidatorContext context, T t) {
        Class[] grouping = GroupingHolder.getGrouping();
        Set<ConstraintViolation<T>> constraintViolations;
        if (grouping == null || grouping.length == 0) {
            constraintViolations = HIBERANTE_VALIDATOR.validate(t);
        } else {
            constraintViolations = HIBERANTE_VALIDATOR.validate(t, grouping);
        }
        if (CollectionUtil.isEmpty(constraintViolations)) {
            return true;
        } else {
            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                context.addError(ValidationError.create("{" + constraintViolation.getPropertyPath() + "} " +
                        constraintViolation.getMessage())
                        .setField(constraintViolation.getPropertyPath().toString())
                        .setInvalidValue(constraintViolation.getInvalidValue()));
            }
            return false;
        }
    }

    @Override
    public void onException(Exception e, ValidatorContext context, T t) {

    }

    public javax.validation.Validator getHiberanteValidator() {
        return HIBERANTE_VALIDATOR;
    }

    public HibernateSupportedValidator<T> setHiberanteValidator(javax.validation.Validator validator) {
        HibernateSupportedValidator.HIBERANTE_VALIDATOR = validator;
        return this;
    }

}