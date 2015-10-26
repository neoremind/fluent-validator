package com.baidu.unbiz.fluentvalidator.demo.validator;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.baidu.unbiz.fluentvalidator.demo.dto.Garage;
import com.baidu.unbiz.fluentvalidator.demo.error.GarageError;
import com.baidu.unbiz.fluentvalidator.support.MessageSupport;
import com.baidu.unbiz.fluentvalidator.util.CollectionUtil;

/**
 * @author zhangxu
 */
public class GarageCarNotExceedLimitValidator extends ValidatorHandler<Garage> implements Validator<Garage> {

    public static final int MAX_CAR_NUM = 50;

    @Override
    public boolean validate(ValidatorContext context, Garage t) {
        if (!CollectionUtil.isEmpty(t.getCarList())) {
            if (t.getCarList().size() > MAX_CAR_NUM) {
                context.addError(
                        ValidationError.create(MessageSupport.getText(GarageError.CAR_NUM_EXCEED_LIMIT.msg(),
                                MAX_CAR_NUM))
                                .setErrorCode(GarageError.CAR_NUM_EXCEED_LIMIT.code())
                                .setField("garage.cars")
                                .setInvalidValue(t.getCarList().size()));
                return false;
            }
        }
        return true;
    }

}
