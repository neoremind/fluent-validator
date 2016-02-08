package com.baidu.unbiz.fluentvalidator.demo.service.impl;

import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toComplex;
import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toSimple;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.DefaultValidateCallback;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.Result;
import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.demo.dto.Car;
import com.baidu.unbiz.fluentvalidator.demo.dto.Garage;
import com.baidu.unbiz.fluentvalidator.demo.exception.CarException;
import com.baidu.unbiz.fluentvalidator.demo.service.GarageService;
import com.baidu.unbiz.fluentvalidator.demo.validator.CarLicensePlateValidator;
import com.baidu.unbiz.fluentvalidator.demo.validator.CarManufacturerValidator;
import com.baidu.unbiz.fluentvalidator.demo.validator.CarSeatCountValidator;
import com.baidu.unbiz.fluentvalidator.demo.validator.GarageCarNotExceedLimitValidator;
import com.baidu.unbiz.fluentvalidator.jsr303.HibernateSupportedValidator;
import com.baidu.unbiz.fluentvalidator.registry.Registry;
import com.baidu.unbiz.fluentvalidator.util.Preconditions;
import com.baidu.unbiz.fluentvalidator.validator.element.ValidatorElementList;

/**
 * @author zhangxu
 */
@Service
public class GarageServiceImpl implements GarageService {

    @Resource
    private Registry springApplicationContextRegistry;

    @Resource
    private javax.validation.Validator hibernateValidator;

    @Override
    public Result addCars(List<Car> cars) {
        Preconditions.checkNotNull(cars, "car should not be null");
        Result result = FluentValidator.checkAll()
                .configure(springApplicationContextRegistry)
                .onEach(cars)
                .doValidate(new DefaultValidateCallback() {
                    @Override
                    public void onFail(ValidatorElementList validatorElementList, List<ValidationError> errors) {
                        throw new CarException(errors.get(0).getErrorMsg());
                    }

                    @Override
                    public void onUncaughtException(Validator validator, Exception e, Object target) throws Exception {
                        throw new CarException(e);
                    }
                })
                .result(toSimple());
        return result;
    }

    @Override
    public Result addCarsThrowException(List<Car> cars) {
        return addCars(cars);
    }

    @Override
    public Result addCar(Car car) {
        Preconditions.checkNotNull(car, "car should not be null");
        Result result = FluentValidator.checkAll().failOver()
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator())
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .doValidate().result(toSimple());
        return result;
    }

    @Override
    public ComplexResult buildGarage(Garage garage) {
        Preconditions.checkNotNull(garage, "garage should not be null");
        ComplexResult result = FluentValidator.checkAll()
                .configure(springApplicationContextRegistry)
                .on(garage, new HibernateSupportedValidator<Garage>().setHiberanteValidator(hibernateValidator))
                .on(garage, new GarageCarNotExceedLimitValidator())
                .onEach(garage.getCarList())
                .doValidate().result(toComplex());
        return result;
    }

}
