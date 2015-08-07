package com.baidu.unbiz.fluentvalidator;

/**
 * 仿闭包，接口中的 {@link #execute(Object...)} 通过回调模拟闭包
 * <p/>
 * 在实际应用中，在验证器内部会调用一些比较耗时操作，例如远程rpc或者数据库调用，而实际结果是可以在线程内共享的，
 * 并供其他业务逻辑使用的。<br/>
 * 在调用发起点，构造闭包，延迟调用到验证逻辑中，同时该闭包缓存了结果对象，那么在调用发起点即可通过{@link #getResult()}获取结果对象。
 *
 * @author zhangxu
 */
@NotThreadSafe
public abstract class ClosureHandler<T> implements Closure<T> {

    /**
     * 是否完成了一次调用，避免重复调用{@link #execute(Object...)}
     */
    private boolean hasExecute = false;

    @Override
    public void execute(Object... input) {
        if (hasExecute == true) {
            return;
        }
        doExecute(input);
        hasExecute = true;
    }

    /**
     * 实际闭包执行逻辑
     *
     * @param input the input to execute on
     */
    public abstract void doExecute(Object... input);

    @Override
    public T executeAndGetResult(Object... input) {
        execute(input);
        return getResult();
    }

}
