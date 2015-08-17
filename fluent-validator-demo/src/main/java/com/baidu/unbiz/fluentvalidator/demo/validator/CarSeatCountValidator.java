package com.baidu.unbiz.fluentvalidator.demo.validator;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.baidu.unbiz.fluentvalidator.demo.error.CarError;

/**
 * @author zhangxu
 */
public class CarSeatCountValidator extends ValidatorHandler<Integer> implements Validator<Integer> {

    @Override
    public boolean validate(ValidatorContext context, Integer t) {
        if (t < 2) {
            context.addErrorMsg(String.format(CarError.SEATCOUNT_ERROR.msg(), t));
            return false;
        }
        return true;
    }

}
