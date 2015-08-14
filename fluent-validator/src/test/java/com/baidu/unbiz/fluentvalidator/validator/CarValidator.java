package com.baidu.unbiz.fluentvalidator.validator;

import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toSimple;

import java.util.List;

import com.baidu.unbiz.fluentvalidator.Closure;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.baidu.unbiz.fluentvalidator.dto.Car;
import com.baidu.unbiz.fluentvalidator.error.CarError;

/**
 * @author zhangxu
 */
public class CarValidator extends ValidatorHandler<Car> implements Validator<Car> {

    @Override
    public boolean validate(ValidatorContext context, Car car) {
        Closure<List<String>> closure = context.getClosure("manufacturerClosure");
        List<String> manufacturers = closure.executeAndGetResult();

        if (!manufacturers.contains(car.getManufacturer())) {
            context.addErrorMsg(String.format(CarError.MANUFACTURER_ERROR.msg(), car.getManufacturer()));
            return false;
        }

        return FluentValidator.checkAll()
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator())
                .doValidate().result(toSimple()).hasError();
    }

}
