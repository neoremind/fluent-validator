package com.baidu.unbiz.fluentvalidator.demo.service;

import java.util.List;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.Result;
import com.baidu.unbiz.fluentvalidator.demo.dto.Car;
import com.baidu.unbiz.fluentvalidator.demo.dto.Garage;

/**
 * @author zhangxu
 */
public interface GarageService2 {

    Garage buildGarage(Garage garage);

    Car addCar(Car cars);

    List<Car> addCars(List<Car> cars);

    List<Car> addCarsThrowException(List<Car> cars);

}
