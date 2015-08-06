package com.baidu.unbiz.fluentvalidator;

/**
 * 仿闭包，接口中的 {@link #execute(Object...)} 通过回调模拟闭包。
 *
 * @author zhangxu
 */
@NotThreadSafe
public abstract class ClosureHandler<T> implements Closure<T> {

    private boolean hasExecute = false;

    public void execute(Object... input) {
        if (hasExecute == true) {
            return;
        }
        doExecute(input);
        hasExecute = true;
    }

    public abstract void doExecute(Object... input);

    public T executeAndGetResult(Object... input) {
        execute(input);
        return getResult();
    }

}
