package com.baidu.unbiz.fluentvalidator;

import com.baidu.unbiz.fluentvalidator.annotation.ThreadSafe;

/**
 * 验证器默认实现
 * <p/>
 * 自定义的验证器如果不想实现{@link Validator}所有方法，可以使用这个默认实现，仅覆盖自己需要实现的方法
 *
 * @author zhangxu
 * @see Validator
 */
@ThreadSafe
public class ValidatorHandler<T> implements Validator<T>, Composable<T> {

    @Override
    public boolean accept(ValidatorContext context, T t) {
        return true;
    }

    @Override
    public boolean validate(ValidatorContext context, T t) {
        return true;
    }

    @Override
    public void onException(Exception e, ValidatorContext context, T t) {

    }

    @Override
    public void compose(FluentValidator current, ValidatorContext context, T t) {
        // extension point for clients to add more validators to the current fluent chain
    }

    /**
     * 验证器的名字，用简单类名称表示
     *
     * @return 名字
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

	@Override
	public boolean validate(ValidatorContext context, T t, String message) {
		// TODO Auto-generated method stub
		return false;
	}

}
