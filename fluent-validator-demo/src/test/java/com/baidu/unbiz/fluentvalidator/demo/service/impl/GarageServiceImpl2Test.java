package com.baidu.unbiz.fluentvalidator.demo.service.impl;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.baidu.unbiz.fluentvalidator.demo.dto.Car;
import com.baidu.unbiz.fluentvalidator.demo.dto.Garage;
import com.baidu.unbiz.fluentvalidator.demo.dto.Owner;
import com.baidu.unbiz.fluentvalidator.demo.error.CarError;
import com.baidu.unbiz.fluentvalidator.demo.exception.CarException;
import com.baidu.unbiz.fluentvalidator.demo.exception.RpcException;
import com.baidu.unbiz.fluentvalidator.demo.rpc.ManufacturerService;
import com.baidu.unbiz.fluentvalidator.demo.service.GarageService2;
import com.baidu.unbiz.fluentvalidator.support.MessageSupport;
import com.google.common.collect.Lists;

/**
 * @author zhangxu
 */
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
public class GarageServiceImpl2Test extends AbstractJUnit4SpringContextTests {

    @Resource
    private GarageService2 garageService;

    @Resource
    private ManufacturerService manufacturerService;

    // Test add multiple cars

    @Test
    public void testAddCars() {
        garageService.addCars(getValidCars());
    }

    @Test
    public void testAddCarsFail() {
        try {
            List<Car> cars = getValidCars();
            cars.get(0).setLicensePlate("xxx123"); // fail fast
            cars.get(1).setSeatCount(0);
            cars.get(1).setManufacturer("XXX");
            garageService.addCars(cars);
        } catch (CarException e) {
            assertThat(e.getClass().getName(), is(CarException.class.getName()));
            assertThat(e.getMessage(), is(String.format(CarError.LICENSEPLATE_ERROR.msg(), "xxx123")));
        }
    }

    @Test
    public void testAddCarsNegativeNull() {
        try {
            garageService.addCars(null);
        } catch (CarException e) {
            assertThat(e.getClass().getName(), is(CarException.class.getName()));
            assertThat(e.getMessage(), is("Cars is empty"));
            return;
        }
        fail();
    }

    @Test
    public void testAddCarsPositiveNull() {
        garageService.addCar(null);
    }

    @Test
    public void testAddCarsUncaughtException() {
        try {
            manufacturerService.setIsMockFail(true);
            List<Car> cars = getValidCars();
            cars.get(1).setManufacturer("XXX");
            garageService.addCarsThrowException(cars);
        } catch (CarException e) {
            assertThat(e.getClass().getName(), is(CarException.class.getName()));
            assertThat(e.getCause().getClass().getName(), is(RpcException.class.getName()));
            assertThat(e.getCause().getMessage(), is("Get all manufacturers failed"));
        } finally {
            manufacturerService.setIsMockFail(false);
        }
    }

    // Test add only one car

    @Test
    public void testAddCar() {
        garageService.addCar(new Car("BMW", "LA1234", 5));
    }

    @Test
    public void testAddCarInvalidLicense() {
        try {
            garageService.addCar(new Car("BMW", "XXXXX1234", 5));
        } catch (CarException e) {
            assertThat(e.getClass().getName(), is(CarException.class.getName()));
            assertThat(e.getMessage(), is(String.format(CarError.LICENSEPLATE_ERROR.msg(), "XXXXX1234")));
        }
    }

    @Test
    public void testAddCarMultipleError() {
        try {
            garageService.addCar(new Car("BMW", "XXXXX1234", 0));
        } catch (CarException e) {
            assertThat(e.getClass().getName(), is(CarException.class.getName()));
            assertThat(e.getMessage(), is(String.format(CarError.LICENSEPLATE_ERROR.msg(), "XXXXX1234")));
        }
    }

    // Test build garage

    @Test
    public void testBuildGarage() {
        Garage garage = getValidGarage();
        garageService.buildGarage(garage);
    }

    @Test
    public void testBuildGarageInvalidName() {
        try {
            Garage garage = getValidGarage();
            garage.setName("abc");
            garageService.buildGarage(garage);
        } catch (CarException e) {
            assertThat(e.getClass().getName(), is(CarException.class.getName()));
            assertThat(e.getMessage(), is("{name} 车库名称长度非法"));
        }
    }

    @Test
    public void testBuildGarageInvalidOwner() {
        try {
            Garage garage = getValidGarage();
            garage.getOwner().setId(0);
            garage.getOwner().setName("hh");  // fail fast
            garageService.buildGarage(garage);
        } catch (CarException e) {
            System.out.println(e.getMessage());
            assertThat(e.getClass().getName(), is(CarException.class.getName()));
        }
    }

    @Test
    public void testBuildGarageCarNumExceed() {
        try {
            Garage garage = getValidGarage();
            List<Car> cars = Lists.newArrayList();
            for (int i = 0; i < 100; i++) {
                cars.add(new Car("BMW", "LA1234", 5));
            }
            garage.setCarList(cars);
            garageService.buildGarage(garage);
        } catch (CarException e) {
            assertThat(e.getClass().getName(), is(CarException.class.getName()));
            assertThat(e.getMessage(), is(MessageSupport.getText("car.size.exceed", 50)));
        }
    }

    @Test
    public void testBuildGarageInvalidCar() {
        try {
            Garage garage = getValidGarage();
            garage.getCarList().get(0).setLicensePlate("xxx");
            garageService.buildGarage(garage);
        } catch (CarException e) {
            assertThat(e.getClass().getName(), is(CarException.class.getName()));
            assertThat(e.getMessage(), is("License is not valid, invalid value=xxx"));
        }
    }

    private Garage getValidGarage() {
        Garage garage = new Garage();
        garage.setCarList(getValidCars());
        garage.setGarageId(100);
        garage.setName("ABCDEFG");
        garage.setOwner(new Owner(99, "Tommy"));
        return garage;
    }

    private List<Car> getValidCars() {
        return Lists.newArrayList(new Car("BMW", "LA1234", 5),
                new Car("Benz", "NYCuuu", 2),
                new Car("Chevrolet", "LA1234", 7));
    }

}