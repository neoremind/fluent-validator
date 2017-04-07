package com.baidu.unbiz.fluentvalidator;

/**
 * 验证器接口。
 * <p/>
 * 泛型<t>T</t>表示待验证对象的类型
 *
 * @author zhangxu
 */
public interface Validator<T> {

    /**
     * 判断在该对象上是否接受或者需要验证
     * <p/>
     * 如果返回true，那么则调用{@link #validate(ValidatorContext, Object)}，否则跳过该验证器
     *
     * @param context 验证上下文
     * @param t       待验证对象
     *
     * @return 是否接受验证
     */
    boolean accept(ValidatorContext context, T t);

    /**
     * 执行验证
     * <p/>
     * 如果发生错误内部需要调用{@link ValidatorContext#addErrorMsg(String)}方法，也即<code>context.addErrorMsg(String)
     * </code>来添加错误，该错误会被添加到结果存根{@link Result}的错误消息列表中。
     *
     * @param context 验证上下文
     * @param t       待验证对象
     *
     * @return 是否验证通过
     */
    boolean validate(ValidatorContext context, T t);
    
    boolean validate(ValidatorContext context, T t,String message);

    /**
     * 异常回调
     * <p/>
     * 当执行{@link #accept(ValidatorContext, Object)}或者{@link #validate(ValidatorContext, Object)}发生异常时的如何处理
     *
     * @param e       异常
     * @param context 验证上下文
     * @param t       待验证对象
     */
    void onException(Exception e, ValidatorContext context, T t);

}
