package com.baidu.unbiz.fluentvalidator.jsr303;

import javax.validation.ConstraintViolation;

import com.baidu.unbiz.fluentvalidator.ValidationError;

/**
 * {@link ConstraintViolation}到{@link ValidationError}的转换器
 *
 * @author zhangxu
 */
public interface ConstraintViolationTransformer {

    /**
     * {@link ConstraintViolation}到{@link ValidationError}的转换
     *
     * @param constraintViolation hibernate的错误
     *
     * @return fluent-validator框架的错误ValidationError
     */
    ValidationError toValidationError(ConstraintViolation constraintViolation);

}
