package com.baidu.unbiz.fluentvalidator.demo.validator;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.baidu.unbiz.fluentvalidator.demo.error.CarError;
import com.baidu.unbiz.fluentvalidator.demo.rpc.ManufacturerService;
import com.baidu.unbiz.fluentvalidator.demo.rpc.impl.ManufacturerServiceImpl;

/**
 * @author zhangxu
 */
@Component
public class CarManufacturerValidator extends ValidatorHandler<String> implements Validator<String> {

    @Resource
    private ManufacturerService manufacturerService = new ManufacturerServiceImpl();

    @Override
    public boolean validate(ValidatorContext context, String t) {
        Boolean ignoreManufacturer = context.getAttribute("ignoreManufacturer", Boolean.class);
        if (ignoreManufacturer != null && ignoreManufacturer) {
            return true;
        }

        if (!manufacturerService.getAllManufacturers().contains(t)) {
            context.addError(ValidationError.create(String.format(CarError.MANUFACTURER_ERROR.msg(), t))
                    .setErrorCode(CarError.MANUFACTURER_ERROR.code())
                    .setField("manufacturer")
                    .setInvalidValue(t));
            return false;
        }
        return true;
    }

}
