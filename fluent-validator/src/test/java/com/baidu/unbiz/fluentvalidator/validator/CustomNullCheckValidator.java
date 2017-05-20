package com.baidu.unbiz.fluentvalidator.validator;


import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;

public class CustomNullCheckValidator extends ValidatorHandler<Object> implements Validator<Object> {

    @Override
    public boolean validate(ValidatorContext context, Object t) {
        if (t == null) {
            context.addErrorMsg(context.getDefaultMessage());
            return false;
        }
        return true;
    }
}
