package com.baidu.unbiz.fluentvalidator;

/**
 * 仿闭包，接口中的 {@link #execute(Object...)} 通过回调模拟闭包。
 *
 * @author zhangxu
 */
public interface Closure<T> {

    /**
     * Performs an action on the specified input object.
     *
     * @param input the input to execute on
     */
    void execute(Object... input);

    /**
     * Get result
     *
     * @return result object
     */
    T getResult();

    /**
     * Wrap {@link #execute(Object...)} and {@link #getResult()}
     *
     * @param input the input to execute on
     *
     * @return result object
     */
    T executeAndGetResult(Object... input);

}
