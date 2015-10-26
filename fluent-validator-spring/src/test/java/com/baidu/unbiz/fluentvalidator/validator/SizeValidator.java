package com.baidu.unbiz.fluentvalidator.validator;

import java.util.List;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.baidu.unbiz.fluentvalidator.dto.Car;
import com.baidu.unbiz.fluentvalidator.error.CarError;
import com.baidu.unbiz.fluentvalidator.support.MessageSupport;

/**
 * @author zhangxu
 */
public class SizeValidator extends ValidatorHandler<List<Car>> implements Validator<List<Car>> {

    @Override
    public boolean validate(ValidatorContext context, List<Car> t) {
        if (t.size() > 10) {
            context.addErrorMsg(MessageSupport.getText(CarError.CAR_SIZE_EXCEED.msg()));
            return false;
        }
        return true;
    }

}
