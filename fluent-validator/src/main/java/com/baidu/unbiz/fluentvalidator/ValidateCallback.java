package com.baidu.unbiz.fluentvalidator;

import java.util.List;

/**
 * @author zhangxu
 */
public interface ValidateCallback {

    void onSuccess(ValidatorElementList chained);

    void onFail(ValidatorElementList chained, List<String> errorMsgs);

    void onUncaughtException(Validator validator, Throwable t, Object target) throws Throwable;

}
