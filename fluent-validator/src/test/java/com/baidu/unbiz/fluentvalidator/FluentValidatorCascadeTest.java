package com.baidu.unbiz.fluentvalidator;

import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toSimple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import com.baidu.unbiz.fluentvalidator.dto.Car;
import com.baidu.unbiz.fluentvalidator.dto.Garage;
import com.baidu.unbiz.fluentvalidator.error.CarError;
import com.baidu.unbiz.fluentvalidator.exception.RuntimeValidateException;
import com.baidu.unbiz.fluentvalidator.validator.CustomNullCheckValidator;
import com.baidu.unbiz.fluentvalidator.validator.NotNullValidator;
import com.google.common.collect.Lists;

/**
 * @author zhangxu
 */
public class FluentValidatorCascadeTest {

    @Test
    public void testGarage() {
        Garage garage = getGarage();

        Result ret = FluentValidator.checkAll()
                .on(garage)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(true));
    }

    @Test
    public void testGarageNegativeStr() {
        Garage garage = getGarage();
        garage.setStr("123");

        Result ret = FluentValidator.checkAll()
                .on(garage)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(1));
        assertThat(ret.getErrors().get(0), is("string should be abc"));
    }

    @Test
    public void testGarageNegativeCollectionCar() {
        Garage garage = getGarage();
        List<Car> cars = getValidCars();
        cars.get(0).setLicensePlate("XXXX");
        garage.setCollectionCar(cars);

        try {
            Result ret = FluentValidator.checkAll()
                    .on(garage)
                    .doValidate()
                    .result(toSimple());
            System.out.println(ret);
        } catch (RuntimeValidateException e) {
            assertThat(e.getCause().getMessage(), is("Call Rpc failed"));
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testGarageNegativeCollectionCar2() {
        Garage garage = getGarage();
        List<Car> cars = getValidCars();
        cars.get(0).setLicensePlate("BEIJING123");
        garage.setCollectionCar(cars);

        Result ret = FluentValidator.checkAll()
                .on(garage)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(1));
        assertThat(ret.getErrors().get(0), is(String.format(CarError.LICENSEPLATE_ERROR.msg(), "BEIJING123")));
    }

    @Test
    public void testGarageNegativeArrayCar() {
        Garage garage = getGarage();
        List<Car> cars = getValidCars();
        cars.get(0).setLicensePlate("BEIJING123");
        garage.setArrayCar(cars.toArray(new Car[] {}));

        Result ret = FluentValidator.checkAll()
                .on(garage)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(1));
        assertThat(ret.getErrors().get(0), is(String.format(CarError.LICENSEPLATE_ERROR.msg(), "BEIJING123")));
    }

    @Test
    public void testGarageNegativeMultiError() {
        Garage garage = getGarage();
        List<Car> cars = getValidCars();
        cars.get(0).setLicensePlate("BEIJING123");
        garage.setCollectionCar(cars);

        List<Car> cars2 = getValidCars();
        cars2.get(0).setSeatCount(0);
        garage.setArrayCar(cars2.toArray(new Car[] {}));

        Result ret = FluentValidator.checkAll().failOver()
                .on(garage)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(2));
    }

    @Test
    public void testGarageNegativeCar() {
        Garage garage = getGarage();
        Car car = getValidCar();
        car.setManufacturer("XXX");
        garage.setSingleCar(car);

        Result ret = FluentValidator.checkAll()
                .on(garage)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(1));
        assertThat(ret.getErrors().get(0), is(String.format(CarError.MANUFACTURER_ERROR.msg(), "XXX")));
    }

    @Test
    public void testGarageNegativeNullListCar() {
        Garage garage = getGarage();
        garage.setCollectionCar(null);
        garage.setArrayCar(null);
        garage.setSingleCar(null);

        Result ret = FluentValidator.checkAll()
                .on(garage)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(true));
    }

    @Test
    public void testGarageNegativeNull() {
        Garage garage = null;

        Result ret = FluentValidator.checkAll()
                .on(garage)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(true));

        ret = FluentValidator.checkAll()
                .on(garage, new NotNullValidator())
                .on(garage)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
    }

    private Garage getGarage() {
        Garage garage = new Garage();
        garage.setCollectionCar(getValidCars());
        garage.setArrayCar(getValidCars().toArray(new Car[] {}));
        garage.setSingleCar(getValidCar());
        garage.setStr("abc");
        return garage;
    }

    private List<Car> getValidCars() {
        return Lists.newArrayList(new Car("BMW", "LA1234", 5),
                new Car("Benz", "NYCuuu", 2),
                new Car("Chevrolet", "LA1234", 7));
    }

    private Car getValidCar() {
        return new Car("BMW", "LA1234", 5);
    }
    
    @Test
    public void testGaragedoValidateWithParameters() {
        Garage garage = null;

        Result ret = FluentValidator.checkAll()
                .on(garage,new CustomNullCheckValidator(),"Garage Object is Null")
                .doValidate(true)
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertEquals(ret.errors.toString(),"[Garage Object is Null]");
        
    }

    
    @Test
    public void testGaragedoValidateWithParametersAdd() {
       Car c = new Car(null,null, 10);
       
        Result ret = FluentValidator.checkAll().failOver()
                .on(c,new CustomNullCheckValidator(),"Car Object is Null")
                .on(c.getLicensePlate() ,new CustomNullCheckValidator(),"Car License is Null")
                .on(c.getManufacturer(),new CustomNullCheckValidator(),"Car Manufactur is Null")
                .on(c.getSeatCount())
                .doValidate(true)
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertEquals(ret.errors.toString(),"[Car License is Null, Car Manufactur is Null]");
        
    }

}
