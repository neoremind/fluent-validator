package com.baidu.unbiz.fluentvalidator;

import java.util.List;

/**
 * 默认验证回调
 * <p/>
 * 如果不想实现{@link ValidateCallback}所有方法，可以使用这个默认实现，仅覆盖自己需要实现的方法
 *
 * @author zhangxu
 * @see ValidateCallback
 */
public class DefaulValidateCallback implements ValidateCallback {

    @Override
    public void onSuccess(ValidatorElementList validatorElementList) {
    }

    @Override
    public void onFail(ValidatorElementList validatorElementList, List<ValidationError> errors) {
    }

    @Override
    public void onUncaughtException(Validator validator, Exception e, Object target) throws Exception {
        throw e;
    }

}
