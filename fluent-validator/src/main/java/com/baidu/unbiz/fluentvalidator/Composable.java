package com.baidu.unbiz.fluentvalidator;

/**
 * 在Validator中添加额外的验证逻辑，用组合的方式
 *
 * @author zhangxu
 */
public interface Composable<T> {

    /**
     * 切入点，可以织入一些校验逻辑
     *
     * @param current 当前的FluentValidator实例
     * @param context 验证器执行调用中的上下文
     * @param t       待验证的对象
     */
    void compose(FluentValidator current, ValidatorContext context, T t);
}
