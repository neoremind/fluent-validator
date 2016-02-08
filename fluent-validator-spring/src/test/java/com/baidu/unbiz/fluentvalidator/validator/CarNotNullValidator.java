package com.baidu.unbiz.fluentvalidator.validator;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.baidu.unbiz.fluentvalidator.error.CarError;
import com.baidu.unbiz.fluentvalidator.support.MethodNameFluentValidatorPostProcessor;

/**
 * @author zhangxu
 */
public class CarNotNullValidator extends ValidatorHandler implements Validator {

    @Override
    public boolean validate(ValidatorContext context, Object t) {
        String methodName = (String) context.getAttribute(MethodNameFluentValidatorPostProcessor.KEY_METHOD_NAME);
        System.out.println("MethodName = " + methodName);
        if (t == null) {
            context.addErrorMsg(CarError.CAR_NULL.msg());
            return false;
        }
        return true;
    }

}
