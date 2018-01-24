package com.baidu.unbiz.fluentvalidator.jsr303;

import java.util.Set;

import javax.validation.ConstraintViolation;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.annotation.NotThreadSafe;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.baidu.unbiz.fluentvalidator.support.GroupingHolder;
import com.baidu.unbiz.fluentvalidator.util.ArrayUtil;
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
    private static javax.validation.Validator HIBERNATE_VALIDATOR;

    /**
     * hibernate默认的错误码
     */
    private int hibernateDefaultErrorCode;

    /**
     * {@link ConstraintViolation}到{@link ValidationError}的转换器
     */
    private ConstraintViolationTransformer constraintViolationTransformer = new DefaultConstraintViolationTransformer();

    @Override
    public boolean accept(ValidatorContext context, T t) {
        return true;
    }

    @Override
    public boolean validate(ValidatorContext context, T t) {
        Class<?>[] groups = GroupingHolder.getGrouping();
        Set<ConstraintViolation<T>> constraintViolations;
        if (ArrayUtil.isEmpty(groups)) {
            constraintViolations = HIBERNATE_VALIDATOR.validate(t);
        } else {
            constraintViolations = HIBERNATE_VALIDATOR.validate(t, groups);
        }
        if (CollectionUtil.isEmpty(constraintViolations)) {
            return true;
        } else {
            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                context.addError(constraintViolationTransformer.toValidationError(constraintViolation)
                        .setErrorCode(hibernateDefaultErrorCode));
            }
            return false;
        }
    }

    @Override
    public void onException(Exception e, ValidatorContext context, T t) {

    }

    public javax.validation.Validator getHiberanteValidator() {
        return HIBERNATE_VALIDATOR;
    }

    /**
     * This is typo method, should use {@link #setHibernateValidator(javax.validation.Validator)}
     */
    @Deprecated
    public HibernateSupportedValidator<T> setHiberanteValidator(javax.validation.Validator validator) {
        HibernateSupportedValidator.HIBERNATE_VALIDATOR = validator;
        return this;
    }

    public HibernateSupportedValidator<T> setHibernateValidator(javax.validation.Validator validator) {
        HibernateSupportedValidator.HIBERNATE_VALIDATOR = validator;
        return this;
    }

    public HibernateSupportedValidator<T> setHibernateDefaultErrorCode(int hibernateDefaultErrorCode) {
        this.hibernateDefaultErrorCode = hibernateDefaultErrorCode;
        return this;
    }
}