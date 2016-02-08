package com.baidu.unbiz.fluentvalidator.service;

import java.util.List;

import com.baidu.unbiz.fluentvalidator.annotation.FluentValid;
import com.baidu.unbiz.fluentvalidator.dto.Car;
import com.baidu.unbiz.fluentvalidator.groups.Add;

/**
 * @author zhangxu
 */
public interface CarService {

    Car addCar(Car car);

    Car addCar(int x, Car car);

    Car addCar(String x, Long y, Car car);

    List<Car> addCars(String x, List<Car> cars);

    Car[] addCars(Car[] cars, Double d);

    List<Car> addCarsWithAddOnChecks(String x, List<Car> cars);

    List<Car> addCarsWithGroups(List<Car> cars);

    List<Car> addCarsWithExcludeGroups(List<Car> cars);

}
