package com.baidu.unbiz.fluentvalidator.demo.service;

import java.util.List;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.Result;
import com.baidu.unbiz.fluentvalidator.demo.dto.Car;
import com.baidu.unbiz.fluentvalidator.demo.dto.Garage;
import com.baidu.unbiz.fluentvalidator.demo.exception.CarException;

/**
 * @author zhangxu
 */
public interface GarageService {

    ComplexResult buildGarage(Garage garage);

    Result addCar(Car cars);

    Result addCars(List<Car> cars);

    Result addCarsThrowException(List<Car> cars);

}
