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
     * 是否验证成功，只要有一个失败就为false
     */
    private boolean isSuccess;

    /**
     * 错误消息列表
     */
    protected List<T> errors;

    @Override
    public String toString() {
        return String.format("Result{isSuccess=%s, errors=%s}", isSuccess(), errors);
    }

    /**
     * 获取错误数量
     *
     * @return 错误数量
     */
    public int getErrorNumber() {
        return CollectionUtil.isEmpty(errors) ? 0 : errors.size();
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public List<T> getErrors() {
        return errors;
    }

    public void setErrors(List<T> errors) {
        this.errors = errors;
    }
}
