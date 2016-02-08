package com.baidu.unbiz.fluentvalidator.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baidu.unbiz.fluentvalidator.annotation.FluentValid;
import com.baidu.unbiz.fluentvalidator.dto.Car;
import com.baidu.unbiz.fluentvalidator.groups.Add;
import com.baidu.unbiz.fluentvalidator.service.CarService;
import com.baidu.unbiz.fluentvalidator.validator.CarNotNullValidator;
import com.baidu.unbiz.fluentvalidator.validator.SizeValidator;

/**
 * @author zhangxu
 */
@Service
public class CarServiceImpl implements CarService {

    @Override
    public Car addCar(@FluentValid Car car) {
        System.out.println("Come on! " + car);
        return car;
    }

    @Override
    public Car addCar(int x, @FluentValid Car car) {
        System.out.println("Come on! " + car);
        return car;
    }

    @Override
    public Car addCar(@FluentValid String x, Long y, @FluentValid Car car) {
        System.out.println("Come on! " + car);
        return car;
    }

    @Override
    public List<Car> addCars(String x, @FluentValid List<Car> cars) {
        System.out.println("Come on! " + cars);
        return cars;
    }

    @Override
    public Car[] addCars(@FluentValid Car[] cars, Double d) {
        System.out.println("Come on! " + cars);
        return cars;
    }

    @Override
    public List<Car> addCarsWithAddOnChecks(String x, @FluentValid({CarNotNullValidator.class, SizeValidator.class})
    List<Car> cars) {
        System.out.println("Come on! " + cars);
        return cars;
    }

    @Override
    public List<Car> addCarsWithGroups(@FluentValid(groups = {Add.class}) List<Car> cars) {
        System.out.println("Come on! " + cars);
        return cars;
    }

    @Override
    public List<Car> addCarsWithExcludeGroups(@FluentValid(excludeGroups = {Add.class}) List<Car> cars) {
        System.out.println("Come on! " + cars);
        return cars;
    }
}
