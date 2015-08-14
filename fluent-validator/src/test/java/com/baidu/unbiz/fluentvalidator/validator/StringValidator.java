package com.baidu.unbiz.fluentvalidator.validator;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;

/**
 * @author zhangxu
 */
public class StringValidator extends ValidatorHandler<String> implements Validator<String> {

    @Override
    public boolean accept(ValidatorContext context, String s) {
        System.out.println("accept " + s);
        return true;
    }

    @Override
    public boolean validate(ValidatorContext context, String t) {
        String myname = context.getAttribute("myname", String.class);
        if (myname != null && myname.equals("pass")) {
            return true;
        }
        System.out.println("check " + t);
        if (!t.startsWith("abc")) {
            context.addError(ValidationError.create("string should be abc").setErrorCode(100).setField("str")
                    .setInvalidValue(t));
            return false;
        }
        return true;
    }

}

