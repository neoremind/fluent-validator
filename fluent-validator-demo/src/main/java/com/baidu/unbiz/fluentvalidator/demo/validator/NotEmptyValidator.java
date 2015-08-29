package com.baidu.unbiz.fluentvalidator.demo.validator;

import java.util.List;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.baidu.unbiz.fluentvalidator.demo.dto.Car;
import com.baidu.unbiz.fluentvalidator.demo.error.CarError;
import com.baidu.unbiz.fluentvalidator.util.CollectionUtil;

/**
 * @author zhangxu
 */
public class NotEmptyValidator extends ValidatorHandler<List<Car>> implements Validator<List<Car>> {

    @Override
    public boolean validate(ValidatorContext context, List<Car> cars) {
        if (CollectionUtil.isEmpty(cars)) {
            context.addErrorMsg("Cars is empty");
            return false;
        }
        return true;
    }

}
