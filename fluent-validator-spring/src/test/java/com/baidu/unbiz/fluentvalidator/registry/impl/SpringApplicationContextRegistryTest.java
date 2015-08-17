package com.baidu.unbiz.fluentvalidator.registry.impl;

import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toSimple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.Result;
import com.baidu.unbiz.fluentvalidator.dto.Car;
import com.baidu.unbiz.fluentvalidator.error.CarError;
import com.baidu.unbiz.fluentvalidator.registry.Registry;

/**
 * @author zhangxu
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class SpringApplicationContextRegistryTest {

    @Resource
    private Registry springApplicationContextRegistry;

    @Test
    public void testCarSeatContErrorFailFast() {
        Car car = getValidCar();
        car.setSeatCount(99);

        Result ret = FluentValidator.checkAll().configure(springApplicationContextRegistry)
                .on(car)
                .doValidate()
                .result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(1));
        assertThat(ret.getErrors().get(0), is(String.format(CarError.SEATCOUNT_ERROR.msg(), 99)));
    }

    private Car getValidCar() {
        return new Car("BMW", "LA1234", 5);
    }

}
