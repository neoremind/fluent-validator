package com.baidu.unbiz.fluentvalidator;

import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toSimple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import com.baidu.unbiz.fluentvalidator.dto.Car;
import com.baidu.unbiz.fluentvalidator.error.CarError;
import com.baidu.unbiz.fluentvalidator.group.CheckManufacturer;
import com.baidu.unbiz.fluentvalidator.registry.impl.SimpleRegistry;
import com.baidu.unbiz.fluentvalidator.util.CollectionUtil;
import com.google.common.collect.Lists;

/**
 * @author zhangxu
 */
public class FluentValidatorAnnotationBasedTest {

    @Test
    public void testCar() {
        Car car = getValidCar();

        Result ret = FluentValidator.checkAll().configure(new SimpleRegistry())
                .on(car)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(true));
    }

    @Test
    public void testCarSeatContErrorFailFast() {
        Car car = getValidCar();
        car.setSeatCount(99);

        Result ret = FluentValidator.checkAll().configure(new SimpleRegistry())
                .on(car)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(1));
        assertThat(ret.getErrors().get(0), is(String.format(CarError.SEATCOUNT_ERROR.msg(), 99)));
    }

    @Test(expected = NullPointerException.class)
    public void testCarNoRegistry() {
        Car car = getValidCar();
        car.setSeatCount(99);

        Result ret = FluentValidator.checkAll().configure(null)
                .on(car)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
    }

    @Test
    public void testCarCollection() {
        List<Car> cars = getValidCars();

        // by default with simple registry
        Result ret = FluentValidator.checkAll()
                .onEach(cars)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(true));
    }

    @Test
    public void testCarCollectionNegative() {
        List<Car> cars = getValidCars();
        cars.get(0).setSeatCount(0);
        cars.get(1).setLicensePlate("BEIJING123");

        Result ret = FluentValidator.checkAll().failOver().configure(new SimpleRegistry())
                .onEach(cars)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(2));
    }

    @Test
    public void testCarArray() {
        Car[] cars = getValidCars().toArray(new Car[] {});

        Result ret = FluentValidator.checkAll().configure(new SimpleRegistry())
                .onEach(cars)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(true));
    }

    @Test
    public void testCarArrayNegative() {
        Car[] cars = getValidCars().toArray(new Car[] {});
        cars[0].setSeatCount(0);
        cars[1].setLicensePlate("BEIJING123");

        Result ret = FluentValidator.checkAll().configure(new SimpleRegistry())
                .failOver()
                .onEach(cars)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(2));
    }

    @Test
    public void testCarCollectionNull() {
        List<Car> cars = null;

        // by default with simple registry
        Result ret = FluentValidator.checkAll()
                .onEach(cars)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(true));
    }

    @Test
    public void testCarCollectionTooMany() {
        List<Car> cars = getValidCarsTooMany();

        Result ret = FluentValidator.checkAll().configure(new SimpleRegistry())
                .onEach(cars)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(true));
    }

    private List<Car> getValidCarsTooMany() {
        List<Car> ret = CollectionUtil.createArrayList();
        for (int i = 0; i < 200; i++) {
            ret.add(new Car("BMW", "LA1234", 5));
        }
        return ret;
    }

    @Test
    public void testCarNull() {
        Car car = null;

        Result ret = FluentValidator.checkAll().configure(new SimpleRegistry())
                .on(car)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(true));
    }

    @Test
    public void testCarGroup() {
        Car car = getValidCar();
        car.setManufacturer("XXXX");

        Result ret = FluentValidator.checkAll(new Class<?>[] {CheckManufacturer.class})
                .configure(new SimpleRegistry())
                .on(car)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrors().get(0), is(String.format(CarError.MANUFACTURER_ERROR.msg(), "XXXX")));
    }

    @Test
    public void testCarGroupNoGroups() {
        Car car = getValidCar();
        car.setManufacturer("XXXX");

        Result ret = FluentValidator.checkAll(new Class<?>[] {})
                .configure(new SimpleRegistry())
                .on(car)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrors().get(0), is(String.format(CarError.MANUFACTURER_ERROR.msg(), "XXXX")));
    }

    @Test
    public void testCarGroupGroupNotMatch() {
        Car car = getValidCar();
        car.setSeatCount(11111);

        Result ret = FluentValidator.checkAll(new Class<?>[] {CheckManufacturer.class})
                .configure(new SimpleRegistry())
                .on(car)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(true));
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
