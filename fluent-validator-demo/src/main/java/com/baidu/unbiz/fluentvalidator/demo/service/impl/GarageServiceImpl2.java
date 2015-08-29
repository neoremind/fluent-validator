package com.baidu.unbiz.fluentvalidator.demo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.Result;
import com.baidu.unbiz.fluentvalidator.annotation.FluentValid;
import com.baidu.unbiz.fluentvalidator.demo.dto.Car;
import com.baidu.unbiz.fluentvalidator.demo.dto.Garage;
import com.baidu.unbiz.fluentvalidator.demo.exception.RpcException;
import com.baidu.unbiz.fluentvalidator.demo.rpc.ManufacturerService;
import com.baidu.unbiz.fluentvalidator.demo.service.GarageService2;

/**
 * 使用拦截器做验证
 *
 * @author zhangxu
 */
@Service
public class GarageServiceImpl2 implements GarageService2 {

    @Resource
    private ManufacturerService manufacturerService;

    @Override
    public List<Car> addCars(@FluentValid List<Car> cars) {
        System.out.println(cars);
        return cars;
    }

    @Override
    public List<Car> addCarsThrowException(@FluentValid List<Car> cars) {
        System.out.println(cars);
        return cars;
    }

    @Override
    public Car addCar(@FluentValid Car car) {
        System.out.println(car);
        return car;
    }

    @Override
    public Garage buildGarage(@FluentValid Garage garage) {
        System.out.println(garage);
        return garage;
    }

}
