package com.baidu.unbiz.fluentvalidator;

import java.util.List;

import com.baidu.unbiz.fluentvalidator.util.CollectionUtil;

/**
 * 错误结果模板抽象类
 * <p/>
 * 提供了一连串“惰性求值”计算后的“及时求值”收殓出口，泛型&lt;T&gt;代表结果类型
 *
 * @author zhangxu
 */
public abstract class GenericResult<T> {

    /**
     * 错误消息列表
     */
    protected List<T> errors;

    @Override
    public String toString() {
        return String.format("Result{hasError=%s, errors=%s}", hasError(), errors);
    }

    /**
     * 获取错误数量
     *
     * @return 错误数量
     */
    public int getErrorNumber() {
        return CollectionUtil.isEmpty(errors) ? 0 : errors.size();
    }

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

    public List<T> getErrors() {
        return errors;
    }

    public void setErrors(List<T> errors) {
        this.errors = errors;
    }
}
