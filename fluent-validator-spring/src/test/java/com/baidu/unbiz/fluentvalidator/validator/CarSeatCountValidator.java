package com.baidu.unbiz.fluentvalidator.validator;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.baidu.unbiz.fluentvalidator.error.CarError;

/**
 * @author zhangxu
 */
public class CarSeatCountValidator extends ValidatorHandler<Integer> implements Validator<Integer> {

    @Override
    public boolean validate(ValidatorContext context, Integer t) {
        if (t != 2 && t != 5 && t != 7) {
            context.addErrorMsg(String.format(CarError.SEATCOUNT_ERROR.msg(), t));
            return false;
        }
        return true;
    }

}
