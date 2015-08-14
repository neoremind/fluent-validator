package com.baidu.unbiz.fluentvalidator;

import java.util.List;

import com.baidu.unbiz.fluentvalidator.annotation.NotThreadSafe;
import com.baidu.unbiz.fluentvalidator.util.CollectionUtil;

/**
 * 内部用验证结果
 *
 * @author zhangxu
 * @see ValidationError
 */
@NotThreadSafe
public class ValidationResult {

    /**
     * 验证错误
     */
    private List<ValidationError> errors;

    /**
     * 验证总体耗时，指通过<code>FluentValidator.doValidate(..)</code>真正“及时求值”过程中的耗时
     */
    private int timeElapsed;

    /**
     * 是否存在错误
     *
     * @return 是否存在错误
     */
    public boolean hasNoError() {
        return CollectionUtil.isEmpty(errors);
    }

    /**
     * 是否存在错误
     *
     * @return 是否存在错误
     *
     * @see #hasNoError()
     */
    public boolean hasError() {
        return !hasNoError();
    }

    /**
     * 添加错误
     *
     * @param error 错误
     */
    public void addError(ValidationError error) {
        if (CollectionUtil.isEmpty(errors)) {
            errors = CollectionUtil.createArrayList(Const.INITIAL_CAPACITY);
        }
        errors.add(error);
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public int getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(int timeElapsed) {
        this.timeElapsed = timeElapsed;
    }
}
