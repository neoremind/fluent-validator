package com.baidu.unbiz.fluentvalidator.validator;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.baidu.unbiz.fluentvalidator.dto.Car;
import com.baidu.unbiz.fluentvalidator.error.CarError;

/**
 * @author zhangxu
 */
public class CarValidator3 extends ValidatorHandler<Car> implements Validator<Car> {

    @Override
    public boolean validate(ValidatorContext context, Car car) {
        if (car.getSeatCount() != 2 && car.getSeatCount() != 5 && car.getSeatCount() != 7) {
            context.addErrorMsg(String.format(CarError.SEATCOUNT_ERROR.msg(), car.getSeatCount()));
            return false;
        }

        return true;
    }

}
