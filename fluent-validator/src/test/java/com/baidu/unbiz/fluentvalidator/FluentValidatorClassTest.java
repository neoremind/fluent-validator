package com.baidu.unbiz.fluentvalidator;

import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toSimple;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import com.baidu.unbiz.fluentvalidator.dto.Garage;
import com.google.common.collect.Lists;
import org.junit.Test;

import com.baidu.unbiz.fluentvalidator.dto.Car;
import com.baidu.unbiz.fluentvalidator.rpc.ManufacturerService;
import com.baidu.unbiz.fluentvalidator.rpc.impl.ManufacturerServiceImpl;
import com.baidu.unbiz.fluentvalidator.validator.CarValidator;
import com.baidu.unbiz.fluentvalidator.validator.CarValidator2;
import com.baidu.unbiz.fluentvalidator.validator.CarValidator3;

/**
 * @author zhangxu
 */
public class FluentValidatorClassTest {

    private ManufacturerService manufacturerService = new ManufacturerServiceImpl();

    /**
     * The following are tested:
     * 1) validator chain
     * 2) closure
     * 3) on wrapper class, not specific fields
     */
    @Test
    public void testPutClosure2Context() {
        Car car = getValidCar();

        Closure<List<String>> closure = new ClosureHandler<List<String>>() {

            private List<String> allManufacturers;

            @Override
            public List<String> getResult() {
                return allManufacturers;
            }

            @Override
            public void doExecute(Object... input) {
                allManufacturers = manufacturerService.getAllManufacturers();
            }
        };

        System.out.println(closure.getResult());
        assertThat(closure.getResult(), nullValue());

        ValidatorChain chain = new ValidatorChain();
        List<Validator> validators = new ArrayList<Validator>();
        validators.add(new CarValidator());
        validators.add(new CarValidator2());
        validators.add(new CarValidator3());
        chain.setValidators(validators);

        Result ret = FluentValidator.checkAll()
                .putClosure2Context("manufacturerClosure", closure)
                .on(car, chain)
                .doValidate().result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(true));

        System.out.println(closure.getResult());
        assertThat(closure.getResult().size(), is(3));
    }

    @Test
    public void testCompose() {
        Garage garage = getGarage();
        garage.setStr("");
        garage.getCollectionCar().get(0).setLicensePlate("OUT_OF_NO_WHERE");
        garage.getCollectionCar().get(2).setSeatCount(-1);

        Result ret = FluentValidator.checkAll()
                .on(garage, new GarageValidator())
                .failOver()
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(3));
        assertThat(ret.getErrors().get(0), is("License is not valid, invalid value=OUT_OF_NO_WHERE"));
        assertThat(ret.getErrors().get(1), is("Seat count is not valid, invalid value=-1"));
        assertThat(ret.getErrors().get(2), is("garage string can not be empty"));
    }

    class GarageValidator extends ValidatorHandler<Garage> implements Validator<Garage> {

        @Override
        public boolean validate(ValidatorContext context, Garage garage) {
            if ("".equals(garage.getStr())) {
                context.addErrorMsg("garage string can not be empty");
                return false;
            }
            return true;
        }

        @Override
        public void compose(FluentValidator current, ValidatorContext context, Garage garage) {
            current.onEach(garage.getCollectionCar(), new CarValidator2())
                    .onEach(garage.getCollectionCar(), new CarValidator3());
        }
    }

    private Garage getGarage() {
        Garage garage = new Garage();
        garage.setCollectionCar(getValidCars());
        garage.setArrayCar(getValidCars().toArray(new Car[]{}));
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

}
