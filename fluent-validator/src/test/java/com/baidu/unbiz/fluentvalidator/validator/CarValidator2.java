package com.baidu.unbiz.fluentvalidator.validator;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.baidu.unbiz.fluentvalidator.dto.Car;
import com.baidu.unbiz.fluentvalidator.error.CarError;

/**
 * @author zhangxu
 */
public class CarValidator2 extends ValidatorHandler<Car> implements Validator<Car> {

    @Override
    public boolean validate(ValidatorContext context, Car car) {
        if (!car.getLicensePlate().startsWith("NYC")
                && !car.getLicensePlate().startsWith("LA")
                && !car.getLicensePlate().startsWith("BEIJING")) {
            context.addErrorMsg(String.format(CarError.LICENSEPLATE_ERROR.msg(), car.getLicensePlate()));
            return false;
        }

        return true;
    }

}
