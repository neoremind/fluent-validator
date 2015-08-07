package com.baidu.unbiz.fluentvalidator;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import com.baidu.unbiz.fluentvalidator.dto.Car;
import com.baidu.unbiz.fluentvalidator.error.CarError;
import com.baidu.unbiz.fluentvalidator.exception.CustomException;
import com.baidu.unbiz.fluentvalidator.validator.CarLicensePlateValidator;
import com.baidu.unbiz.fluentvalidator.validator.CarManufacturerValidator;
import com.baidu.unbiz.fluentvalidator.validator.CarSeatCountValidator;

/**
 * @author zhangxu
 */
public class FluentValidatorPropertyTest {

    @Test
    public void testCar() {
        Car car = getValidCar();

        Result ret = FluentValidator.checkAll()
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator())
                .doValidate();
        System.out.println(ret);
        assertThat(ret.hasNoError(), is(true));
    }

    @Test
    public void testCarSeatContErrorFailFast() {
        Car car = getValidCar();
        car.setSeatCount(99);

        Result ret = FluentValidator.checkAll()
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator()).failFast()
                .doValidate();
        System.out.println(ret);
        assertThat(ret.hasNoError(), is(false));
        assertThat(ret.getErrorNumber(), is(1));
        assertThat(ret.getErrorMsgs().get(0), is(String.format(CarError.SEATCOUNT_ERROR.msg(), 99)));
    }

    @Test
    public void testCarManufacturerError() {
        Car car = getValidCar();
        car.setManufacturer("XXXX");

        Result ret = FluentValidator.checkAll()
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator())
                .doValidate();
        System.out.println(ret);
        assertThat(ret.hasNoError(), is(false));
        assertThat(ret.getErrorNumber(), is(1));
        assertThat(ret.getErrorMsgs().get(0), is(String.format(CarError.MANUFACTURER_ERROR.msg(), "XXXX")));
    }

    @Test
    public void testCarPutContext() {
        Car car = getValidCar();
        car.setManufacturer("XXXX");

        Result ret = FluentValidator.checkAll()
                .putAttribute2Context("ignoreManufacturer", true)
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator())
                .doValidate();
        System.out.println(ret);
        assertThat(ret.hasNoError(), is(true));
    }

    @Test(expected = RuntimeValidateException.class)
    public void testCarLicensePlateErrorRuntimeException() {
        Car car = getValidCar();
        car.setLicensePlate("YYYY");

        Result ret = FluentValidator.checkAll()
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator())
                .doValidate();
        System.out.println(ret);
        assertThat(ret.hasNoError(), is(false));
        assertThat(ret.getErrorNumber(), is(1));
        assertThat(ret.getErrorMsgs().get(0), is(String.format(CarError.MANUFACTURER_ERROR.msg(), "XXXX")));
    }

    @Test
    public void testCarLicensePlateError() {
        Car car = getValidCar();
        car.setLicensePlate("BEIJING");

        Result ret = FluentValidator.checkAll()
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator()).failFast()
                .doValidate();
        System.out.println(ret);
        assertThat(ret.hasNoError(), is(false));
        assertThat(ret.getErrorNumber(), is(1));
        assertThat(ret.getErrorMsgs().get(0), is(String.format(CarError.LICENSEPLATE_ERROR.msg(), "BEIJING")));
    }

    @Test
    public void testFailOver() {
        Car car = getValidCar();
        car.setLicensePlate("BEIJING");
        car.setManufacturer("XXXX");
        car.setSeatCount(99);

        Result ret = FluentValidator.checkAll().failOver()
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator())
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .doValidate();
        System.out.println(ret);
        assertThat(ret.hasNoError(), is(false));
        assertThat(ret.getErrorNumber(), is(3));
        assertThat(ret.getErrorMsgs().get(0), is(String.format(CarError.MANUFACTURER_ERROR.msg(), "XXXX")));
    }

    @Test
    public void testWhen() {
        Car car = getValidCar();
        car.setLicensePlate("BEIJING");
        car.setManufacturer("XXXX");
        car.setSeatCount(99);

        Result ret = FluentValidator.checkAll().failOver()
                .on(car.getManufacturer(), new CarManufacturerValidator()).when(true)
                .on(car.getSeatCount(), new CarSeatCountValidator())
                .on(car.getLicensePlate(), new CarLicensePlateValidator()).when(false)
                .doValidate();
        System.out.println(ret);
        assertThat(ret.hasNoError(), is(false));
        assertThat(ret.getErrorNumber(), is(2));
        assertThat(ret.getErrorMsgs().get(0), is(String.format(CarError.MANUFACTURER_ERROR.msg(), "XXXX")));
    }

    @Test
    public void testDoValidateCallbackOnSuccess() {
        Car car = getValidCar();

        final String[] ref = new String[1];
        Result ret = FluentValidator.checkAll()
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator())
                .doValidate(new DefaulValidateCallback() {
                    @Override
                    public void onSuccess(ValidatorElementList chained) {
                        ref[0] = "all ok!";
                    }
                });
        System.out.println(ret);
        assertThat(ret.hasNoError(), is(true));
        assertThat(ref[0], is("all ok!"));
    }

    @Test(expected = CustomException.class)
    public void testDoValidateCallbackOnFail() {
        Car car = getValidCar();
        car.setSeatCount(99);
        car.setManufacturer("XXXX");

        final int[] ref = new int[1];
        Result ret = FluentValidator.checkAll().failOver()
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator())
                .doValidate(new DefaulValidateCallback() {
                    @Override
                    public void onFail(ValidatorElementList chained, List<String> errorMsgs) {
                        ref[0] = errorMsgs.size();
                        throw new CustomException("ERROR HERE");
                    }
                });
        System.out.println(ret);
        assertThat(ret.hasNoError(), is(false));
        assertThat(ref[0], is(2));
    }

    @Test
    public void testErrorDoValidateCallbackOnUncaughtException() {
        Car car = getValidCar();
        car.setLicensePlate("YYYY");

        try {
            Result ret = FluentValidator.checkAll().failFast()
                    .on(car.getLicensePlate(), new CarLicensePlateValidator())
                    .on(car.getManufacturer(), new CarManufacturerValidator())
                    .on(car.getSeatCount(), new CarSeatCountValidator())
                    .doValidate(new DefaulValidateCallback() {
                        @Override
                        public void onUncaughtException(Validator validator, Exception e, Object target)
                                throws Exception {
                            System.err.println("Fetal here!");
                        }
                    });
            System.out.println(ret);
        } catch (RuntimeValidateException e) {
            assertThat(e.getCause().getMessage(), is("Call Rpc failed"));
        }
    }

    private Car getValidCar() {
        return new Car("BMW", "LA1234", 5);
    }

}