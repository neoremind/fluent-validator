package com.baidu.unbiz.fluentvalidator.interceptor;

import java.util.List;

import com.baidu.unbiz.fluentvalidator.DefaultValidateCallback;
import com.baidu.unbiz.fluentvalidator.ValidateCallback;
import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.exception.CarException;
import com.baidu.unbiz.fluentvalidator.validator.element.ValidatorElementList;

/**
 * @author zhangxu
 */
public class ValidateCarCallback extends DefaultValidateCallback implements ValidateCallback {

    @Override
    public void onFail(ValidatorElementList validatorElementList, List<ValidationError> errors) {
        throw new CarException(errors.get(0).getErrorMsg());
    }

    @Override
    public void onSuccess(ValidatorElementList validatorElementList) {
        System.out.println("Everything works fine!");
    }

    @Override
    public void onUncaughtException(Validator validator, Exception e, Object target) throws Exception {
        throw new CarException(e);
    }
}
