package com.baidu.unbiz.fluentvalidator.demo.validator;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.baidu.unbiz.fluentvalidator.demo.error.CarError;

/**
 * @author zhangxu
 */
public class CarLicensePlateValidator extends ValidatorHandler<String> implements Validator<String> {

    @Override
    public boolean validate(ValidatorContext context, String t) {
        if (t.startsWith("NYC") || t.startsWith("LA") || t.startsWith("BJ")) {
            return true;
        }
        context.addError(ValidationError.create(String.format(CarError.LICENSEPLATE_ERROR.msg(), t))
                .setErrorCode(CarError.LICENSEPLATE_ERROR.code())
                .setField("licensePlate")
                .setInvalidValue(t));
        return false;
    }

}
