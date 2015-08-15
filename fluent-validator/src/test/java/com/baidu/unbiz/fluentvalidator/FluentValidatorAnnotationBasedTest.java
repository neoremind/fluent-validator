package com.baidu.unbiz.fluentvalidator;

import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toSimple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.baidu.unbiz.fluentvalidator.dto.Car;
import com.baidu.unbiz.fluentvalidator.error.CarError;
import com.baidu.unbiz.fluentvalidator.exception.RuntimeValidateException;
import com.baidu.unbiz.fluentvalidator.registry.impl.SimpleRegistry;

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
        assertThat(ret.hasNoError(), is(true));
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
        assertThat(ret.hasNoError(), is(false));
        assertThat(ret.getErrorNumber(), is(1));
        assertThat(ret.getErrors().get(0), is(String.format(CarError.SEATCOUNT_ERROR.msg(), 99)));
    }

    @Test(expected = RuntimeValidateException.class)
    public void testCarNoRegistry() {
        Car car = getValidCar();
        car.setSeatCount(99);

        Result ret = FluentValidator.checkAll()
                .on(car)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
    }

    private Car getValidCar() {
        return new Car("BMW", "LA1234", 5);
    }

}
