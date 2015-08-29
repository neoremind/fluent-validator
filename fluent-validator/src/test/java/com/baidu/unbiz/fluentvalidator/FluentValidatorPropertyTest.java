package com.baidu.unbiz.fluentvalidator;

import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toSimple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import com.baidu.unbiz.fluentvalidator.dto.Car;
import com.baidu.unbiz.fluentvalidator.error.CarError;
import com.baidu.unbiz.fluentvalidator.exception.CustomException;
import com.baidu.unbiz.fluentvalidator.exception.RuntimeValidateException;
import com.baidu.unbiz.fluentvalidator.validator.CarLicensePlateValidator;
import com.baidu.unbiz.fluentvalidator.validator.CarManufacturerValidator;
import com.baidu.unbiz.fluentvalidator.validator.CarSeatCountValidator;
import com.baidu.unbiz.fluentvalidator.validator.element.ValidatorElementList;

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
                .doValidate().result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(true));
    }

    @Test
    public void testCarSeatContErrorFailFast() {
        Car car = getValidCar();
        car.setSeatCount(99);

        Result ret = FluentValidator.checkAll()
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator()).failFast()
                .doValidate().result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(1));
        assertThat(ret.getErrors().get(0), is(String.format(CarError.SEATCOUNT_ERROR.msg(), 99)));
    }

    @Test
    public void testCarManufacturerError() {
        Car car = getValidCar();
        car.setManufacturer("XXXX");

        Result ret = FluentValidator.checkAll()
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator())
                .doValidate().result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(1));
        assertThat(ret.getErrors().get(0), is(String.format(CarError.MANUFACTURER_ERROR.msg(), "XXXX")));
    }

    @Test
    public void testCarPutAttribute2Context() {
        Car car = getValidCar();
        car.setManufacturer("XXXX");

        Result ret = FluentValidator.checkAll()
                .putAttribute2Context("ignoreManufacturer", true)
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator())
                .doValidate().result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(true));
    }

    @Test(expected = RuntimeValidateException.class)
    public void testCarLicensePlateErrorRuntimeException() {
        Car car = getValidCar();
        car.setLicensePlate("YYYY");

        Result ret = FluentValidator.checkAll()
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator())
                .doValidate().result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(1));
        assertThat(ret.getErrors().get(0), is(String.format(CarError.MANUFACTURER_ERROR.msg(), "XXXX")));
    }

    @Test
    public void testCarLicensePlateError() {
        Car car = getValidCar();
        car.setLicensePlate("BEIJING");

        Result ret = FluentValidator.checkAll()
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator()).failFast()
                .doValidate().result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(1));
        assertThat(ret.getErrors().get(0), is(String.format(CarError.LICENSEPLATE_ERROR.msg(), "BEIJING")));
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
                .doValidate().result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(3));
        assertThat(ret.getErrors().get(0), is(String.format(CarError.MANUFACTURER_ERROR.msg(), "XXXX")));
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
                .doValidate().result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(2));
        assertThat(ret.getErrors().get(0), is(String.format(CarError.MANUFACTURER_ERROR.msg(), "XXXX")));
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
                    public void onSuccess(ValidatorElementList validatorElementList) {
                        ref[0] = "all ok!";
                    }
                }).result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(true));
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
                    public void onFail(ValidatorElementList validatorElementList, List<ValidationError> errors) {
                        ref[0] = errors.size();
                        throw new CustomException("ERROR HERE");
                    }
                }).result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ref[0], is(2));
    }

    @Test
    public void testErrorDoValidateCallbackOnUncaughtException() {
        Car car = getValidCar();
        car.setLicensePlate("YYYY");

        final StringBuilder uncaughtMsg = new StringBuilder();
        try {
            Result ret = FluentValidator.checkAll().failFast()
                    .on(car.getLicensePlate(), new CarLicensePlateValidator())
                    .on(car.getManufacturer(), new CarManufacturerValidator())
                    .on(car.getSeatCount(), new CarSeatCountValidator())
                    .doValidate(new DefaulValidateCallback() {
                        @Override
                        public void onUncaughtException(Validator validator, Exception e, Object target)
                                throws Exception {
                            uncaughtMsg.append(String.format("Fetal here on %s when validating %s ending error of %s",
                                    validator, target, e.getMessage()));
                        }
                    }).result(toSimple());
            System.out.println(ret);
        } catch (RuntimeValidateException e) {
            System.out.println(uncaughtMsg.toString());
            assertThat(uncaughtMsg.toString(),
                    is("Fetal here on CarLicensePlateValidator when validating YYYY ending error of "
                            + "Call Rpc failed"));
            assertThat(e.getCause().getMessage(), is("Call Rpc failed"));
        }
    }

    @Test
    public void testCarNullProperty() {
        Car car = getValidCar();
        car.setLicensePlate(null);

        try {
            Result ret = FluentValidator.checkAll()
                    .on(car.getLicensePlate(), new CarLicensePlateValidator())
                    .on(car.getManufacturer(), new CarManufacturerValidator())
                    .on(car.getSeatCount(), new CarSeatCountValidator())
                    .doValidate().result(toSimple());
            System.out.println(ret);
            assertThat(ret.isSuccess(), is(true));
        } catch (RuntimeValidateException e) {
            assertThat(e.getCause().toString(), is("java.lang.NullPointerException"));
        }

    }

    private Car getValidCar() {
        return new Car("BMW", "LA1234", 5);
    }

}