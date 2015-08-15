package com.baidu.unbiz.fluentvalidator.validator;

import org.springframework.stereotype.Component;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.baidu.unbiz.fluentvalidator.error.CarError;
import com.baidu.unbiz.fluentvalidator.rpc.ManufacturerService;
import com.baidu.unbiz.fluentvalidator.rpc.impl.ManufacturerServiceImpl;

/**
 * @author zhangxu
 */
@Component
public class CarManufacturerValidator extends ValidatorHandler<String> implements Validator<String> {

    private ManufacturerService manufacturerService = new ManufacturerServiceImpl();

    @Override
    public boolean validate(ValidatorContext context, String t) {
        Boolean ignoreManufacturer = context.getAttribute("ignoreManufacturer", Boolean.class);
        if (ignoreManufacturer != null && ignoreManufacturer) {
            return true;
        }
        if (!manufacturerService.getAllManufacturers().contains(t)) {
            context.addErrorMsg(String.format(CarError.MANUFACTURER_ERROR.msg(), t));
            return false;
        }
        return true;
    }

}
