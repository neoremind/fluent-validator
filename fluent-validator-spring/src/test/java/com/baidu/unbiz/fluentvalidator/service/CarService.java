package com.baidu.unbiz.fluentvalidator.service;

import java.util.List;

import com.baidu.unbiz.fluentvalidator.annotation.FluentValid;
import com.baidu.unbiz.fluentvalidator.dto.Car;

/**
 * @author zhangxu
 */
public interface CarService {

    Car addCar(Car car);

    Car addCar(int x, @FluentValid Car car);

    Car addCar(@FluentValid String x, Long y, @FluentValid Car car);

    List<Car> addCars(String x, @FluentValid List<Car> cars);

    Car[] addCars(Car[] cars, Double d);

}
