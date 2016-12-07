package com.baidu.unbiz.fluentvalidator;

import java.util.Map;

import com.baidu.unbiz.fluentvalidator.annotation.FluentValid;
import com.baidu.unbiz.fluentvalidator.util.CollectionUtil;
import com.baidu.unbiz.fluentvalidator.util.Function;

/**
 * 验证器执行调用中的上下文
 * <p/>
 * 在验证过程中{@link Validator#validate(ValidatorContext, Object)}以及{@link Validator#accept(ValidatorContext,
 * Object)}使用，主要用途在于:
 * <ul>
 * <li>1. 调用发起点可以放入一些变量或者对象，所有验证器均可以共享使用。</li>
 * <li>2. 调用发起点可以做闭包，将一些验证过程中可以复用的结果对象缓存住（一般是比较耗时才可以获取到的），在发起点可以获取。</li>
 * <li>3. 代理添加错误信息</li>
 * </ul>
 *
 * @author zhangxu
 * @see Closure
 * @see Result
 */
public class ValidatorContext {

    /**
     * 验证器均可以共享使用的属性键值对
     */
    private Map<String, Object> attributes;

    /**
     * 调用发起点注入的闭包
     */
    private Map<String, Closure> closures;

    /**
     * 调用结果对象
     */
    public ValidationResult result;

    public boolean validateWith(Function<FluentValidator, FluentValidator> configuration) {
        ComplexResult localResult = configuration.apply(FluentValidator.checkAll())
                .failOver()
                .doValidate()
                .result(ResultCollectors.toComplex());

        for (ValidationError error : localResult.getErrors()) {
            addError(error);
        }

        return localResult.isSuccess();
    }

    /**
     * 添加错误信息
     *
     * @param msg 错误信息
     */
    public void addErrorMsg(String msg) {
        result.addError(ValidationError.create(msg));
    }

    /**
     * 添加错误信息
     *
     * @param validationError 验证错误
     */
    public void addError(ValidationError validationError) {
        result.addError(validationError);
    }

    /**
     * 获取属性
     *
     * @param key 键
     *
     * @return 值
     */
    public Object getAttribute(String key) {
        if (attributes != null && !attributes.isEmpty()) {
            return attributes.get(key);
        }
        return null;

    }

    /**
     * 根据类型<t>T</t>直接获取属性值
     *
     * @param key   键
     * @param clazz 值类型
     *
     * @return 值
     */
    public <T> T getAttribute(String key, Class<T> clazz) {
        return (T) getAttribute(key);
    }

    public void setAttribute(String key, Object value) {
        if (attributes == null) {
            attributes = CollectionUtil.createHashMap(Const.INITIAL_CAPACITY);
        }
        attributes.put(key, value);
    }

    /**
     * 获取闭包
     *
     * @param key 闭包名称
     *
     * @return 闭包
     */
    public Closure getClosure(String key) {
        if (closures != null && !closures.isEmpty()) {
            return closures.get(key);
        }
        return null;
    }

    /**
     * 注入闭包
     *
     * @param key     闭包名称
     * @param closure 闭包
     */
    public void setClosure(String key, Closure closure) {
        if (closures == null) {
            closures = CollectionUtil.createHashMap(Const.INITIAL_CAPACITY);
        }
        closures.put(key, closure);
    }

    /**
     * 设置验证结果
     *
     * @param result 验证结果
     */
    public void setResult(ValidationResult result) {
        this.result = result;
    }
}
