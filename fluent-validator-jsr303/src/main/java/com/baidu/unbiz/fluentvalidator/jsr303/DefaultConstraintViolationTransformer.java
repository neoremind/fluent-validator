package com.baidu.unbiz.fluentvalidator.jsr303;

import javax.validation.ConstraintViolation;

import com.baidu.unbiz.fluentvalidator.ValidationError;

/**
 * 默认的{@link ConstraintViolation}到{@link ValidationError}的转换器
 *
 * @author zhangxu
 */
public class DefaultConstraintViolationTransformer implements ConstraintViolationTransformer {

    @Override
    public ValidationError toValidationError(ConstraintViolation constraintViolation) {
        return ValidationError.create(constraintViolation.getMessage())
                .setField(constraintViolation.getPropertyPath().toString())
                .setInvalidValue(constraintViolation.getInvalidValue());
    }

}
