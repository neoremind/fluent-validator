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

    T getResult();

    T executeAndGetResult(Object... input);

}
