package com.baidu.unbiz.fluentvalidator.demo.validator;

import java.util.List;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.baidu.unbiz.fluentvalidator.demo.dto.Car;
import com.baidu.unbiz.fluentvalidator.demo.dto.Garage;
import com.baidu.unbiz.fluentvalidator.demo.error.GarageError;
import com.baidu.unbiz.fluentvalidator.util.CollectionUtil;

/**
 * @author zhangxu
 */
public class CarNotExceedLimitValidator extends ValidatorHandler<List<Car>> implements Validator<List<Car>> {

    public static final int MAX_CAR_NUM = 50;

    @Override
    public boolean validate(ValidatorContext context, List<Car> cars) {
        if (cars.size() > MAX_CAR_NUM) {
            context.addError(
                    ValidationError.create(String.format(GarageError.CAR_NUM_EXCEED_LIMIT.msg(), MAX_CAR_NUM))
                            .setErrorCode(GarageError.CAR_NUM_EXCEED_LIMIT.code())
                            .setField("garage.cars")
                            .setInvalidValue(cars.size()));
            return false;
        }
        return true;
    }

}
