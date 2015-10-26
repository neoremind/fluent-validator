package com.baidu.unbiz.fluentvalidator.demo.service.impl;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.Result;
import com.baidu.unbiz.fluentvalidator.demo.Application;
import com.baidu.unbiz.fluentvalidator.demo.dto.Car;
import com.baidu.unbiz.fluentvalidator.demo.dto.Garage;
import com.baidu.unbiz.fluentvalidator.demo.dto.Owner;
import com.baidu.unbiz.fluentvalidator.demo.error.CarError;
import com.baidu.unbiz.fluentvalidator.demo.exception.CarException;
import com.baidu.unbiz.fluentvalidator.demo.exception.RpcException;
import com.baidu.unbiz.fluentvalidator.demo.rpc.ManufacturerService;
import com.baidu.unbiz.fluentvalidator.demo.service.GarageService;
import com.baidu.unbiz.fluentvalidator.support.MessageSupport;
import com.google.common.collect.Lists;

/**
 * @author zhangxu
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class GarageServiceImplTest {

    @Resource(name = "garageServiceImpl")
    private GarageService garageService;

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
    @Ignore
    //TODO
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
        Result result = garageService.addCar(new Car("BMW", "LA1234", 5));
        System.out.println(result);
        assertThat(result.isSuccess(), is(true));
    }

    @Test
    public void testAddCarInvalidLicense() {
        Result result = garageService.addCar(new Car("BMW", "XXXXX1234", 5));
        System.out.println(result);
        assertThat(result.isSuccess(), is(false));
        assertThat(result.getErrorNumber(), is(1));
        assertThat(result.getErrors().get(0), is(String.format(CarError.LICENSEPLATE_ERROR.msg(), "XXXXX1234")));
    }

    @Test
    public void testAddCarMultipleError() {
        Result result = garageService.addCar(new Car("BMW", "XXXXX1234", 0));
        System.out.println(result);
        assertThat(result.isSuccess(), is(false));
        assertThat(result.getErrorNumber(), is(2));
        assertThat(result.getErrors().get(0), is(String.format(CarError.SEATCOUNT_ERROR.msg(), 0)));
        assertThat(result.getErrors().get(1), is(String.format(CarError.LICENSEPLATE_ERROR.msg(), "XXXXX1234")));
    }

    // Test build garage

    @Test
    public void testBuildGarage() {
        Garage garage = getValidGarage();
        ComplexResult result = garageService.buildGarage(garage);
        System.out.println(result);
        assertThat(result.isSuccess(), is(true));
    }

    @Test
    public void testBuildGarageInvalidName() {
        Garage garage = getValidGarage();
        garage.setName("abc");
        ComplexResult result = garageService.buildGarage(garage);
        System.out.println(result);
        assertThat(result.isSuccess(), is(false));
        assertThat(result.getErrorNumber(), is(1));
        assertThat(result.getErrors().get(0).getErrorMsg(), is("{name} garage name length is invalid"));
    }

    @Test
    public void testBuildGarageInvalidOwner() {
        Garage garage = getValidGarage();
        garage.getOwner().setId(0);
        garage.getOwner().setName("hh");
        ComplexResult result = garageService.buildGarage(garage);
        System.out.println(result);
        assertThat(result.isSuccess(), is(false));
        assertThat(result.getErrorNumber(), is(2));
    }

    @Test
    public void testBuildGarageCarNumExceed() {
        Garage garage = getValidGarage();
        List<Car> cars = Lists.newArrayList();
        for (int i = 0; i < 100; i++) {
            cars.add(new Car("BMW", "LA1234", 5));
        }
        garage.setCarList(cars);
        ComplexResult result = garageService.buildGarage(garage);
        System.out.println(result);
        assertThat(result.isSuccess(), is(false));
        assertThat(result.getErrorNumber(), is(1));
        assertThat(result.getErrors().get(0).getErrorMsg(), is(MessageSupport.getText("car.size.exceed", 50)));
    }

    @Test
    public void testBuildGarageInvalidCar() {
        Garage garage = getValidGarage();
        garage.getCarList().get(0).setLicensePlate("xxx");
        ComplexResult result = garageService.buildGarage(garage);
        System.out.println(result);
        assertThat(result.isSuccess(), is(false));
        assertThat(result.getErrorNumber(), is(1));
        assertThat(result.getErrors().get(0).getErrorMsg(), is("License is not valid, invalid value=xxx"));
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