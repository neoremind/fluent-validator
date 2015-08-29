package com.baidu.unbiz.fluentvalidator.demo.validator;

import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toComplex;
import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toSimple;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.Result;
import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.baidu.unbiz.fluentvalidator.demo.dto.Car;
import com.baidu.unbiz.fluentvalidator.demo.error.CarError;
import com.baidu.unbiz.fluentvalidator.demo.error.GarageError;
import com.baidu.unbiz.fluentvalidator.registry.impl.SpringApplicationContextRegistry;
import com.baidu.unbiz.fluentvalidator.util.CollectionUtil;

/**
 * @author zhangxu
 */
@Component
public class CarValidator extends ValidatorHandler<List<Car>> implements Validator<List<Car>> {

    public static final int MAX_CAR_NUM = 50;

    @Resource
    private SpringApplicationContextRegistry registry;

    @Override
    public boolean validate(ValidatorContext context, List<Car> cars) {
        ComplexResult result = FluentValidator.checkAll().configure(registry)
                .onEach(cars)
                .doValidate()
                .result(toComplex());
        if (!result.isSuccess()) {
            ValidationError error = result.getErrors().get(0);
            context.addError(error);
            return false;
        }

        return true;
    }
}
