package com.baidu.unbiz.fluentvalidator.interceptor;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.baidu.unbiz.fluentvalidator.dto.Car;
import com.baidu.unbiz.fluentvalidator.exception.CarException;
import com.baidu.unbiz.fluentvalidator.service.CarService;
import com.google.common.collect.Lists;

/**
 * @author zhangxu
 */
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
public class FluentValidateInterceptorTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private CarService carService;

    @Test
    public void testAddCar() {
        Car car = getValidCar();
        carService.addCar(car);
    }

    @Test
    public void testAddCar2() {
        Car car = getValidCar();
        carService.addCar(1, car);
    }

    @Test
    public void testAddCar3() {
        Car car = getValidCar();
        carService.addCar("abc", 9L, car);
    }

    @Test
    public void testAddCarList() {
        List<Car> cars = getValidCars();
        carService.addCars("abc", cars);
    }

    @Test
    public void testAddCarListNull() {
        List<Car> cars = null;
        carService.addCars("abc", cars);
    }

    @Test
    public void testAddCarArray() {
        Car[] cars = getValidCars().toArray(new Car[] {});
        carService.addCars(cars, 9d);
    }

    @Test
    public void testAddCarArrayNull() {
        Car[] cars = null;
        carService.addCars(cars, 9d);
    }

    @Test(expected = CarException.class)
    public void testAddCarNegative() {
        Car car = getValidCar();
        car.setLicensePlate("BEIJING123");
        carService.addCar(car);
    }

    @Test(expected = CarException.class)
    public void testAddCarNegative2() {
        Car car = getValidCar();
        car.setLicensePlate("XXXX");
        carService.addCar(car);
    }

    private Car getValidCar() {
        return new Car("BMW", "LA1234", 5);
    }

    private List<Car> getValidCars() {
        return Lists.newArrayList(new Car("BMW", "LA1234", 5),
                new Car("Benz", "NYCuuu", 2),
                new Car("Chevrolet", "LA1234", 7));
    }

}
