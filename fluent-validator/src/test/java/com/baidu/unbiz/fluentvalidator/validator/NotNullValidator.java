package com.baidu.unbiz.fluentvalidator.validator;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;

/**
 * @author zhangxu
 */
public class NotNullValidator extends ValidatorHandler implements Validator {

    @Override
    public boolean validate(ValidatorContext context, Object t) {
        if (t == null) {
            context.addErrorMsg("Null is not expected!");
            return false;
        }
        return true;
    }

}
