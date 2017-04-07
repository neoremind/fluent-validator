package com.baidu.unbiz.fluentvalidator;

import com.baidu.unbiz.fluentvalidator.dto.Car;
import com.baidu.unbiz.fluentvalidator.util.Decorator;
import com.baidu.unbiz.fluentvalidator.validator.CarLicensePlateValidator;
import com.baidu.unbiz.fluentvalidator.validator.CarManufacturerValidator;
import com.baidu.unbiz.fluentvalidator.validator.CarSeatCountValidator;
import org.junit.Test;

import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toSimple;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author zhangxu
 */
public class QuickValidatorTest {

    @Test
    public void testCarComplexResult() {
        final Car car = getValidCar();

        ComplexResult ret = QuickValidator.doAndGetComplexResult(new Decorator() {
            @Override
            public FluentValidator decorate(FluentValidator fv) {
                return fv.on(car.getLicensePlate(), new CarLicensePlateValidator())
                        .on(car.getManufacturer(), new CarManufacturerValidator())
                        .on(car.getSeatCount(), new CarSeatCountValidator());
            }
        });
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(true));
        assertThat(ret.getErrors(), nullValue());
    }

    @Test
    public void testCarComplexResult2() {
        final Car car = getValidCar();

        ComplexResult2 ret = QuickValidator.doAndGetComplexResult2(new Decorator() {
            @Override
            public FluentValidator decorate(FluentValidator fv) {
                return fv.on(car.getLicensePlate(), new CarLicensePlateValidator())
                        .on(car.getManufacturer(), new CarManufacturerValidator())
                        .on(car.getSeatCount(), new CarSeatCountValidator());
            }
        });
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(true));
        assertThat(ret.getErrors(), notNullValue());
        assertThat(ret.getErrors().isEmpty(), is(true));
    }

    @Test
    public void testPassContextToInnerValidatorWithoutSharedContext() {
        Car car = getValidCar();
        car.setSeatCount(0);

        Result ret = FluentValidator.checkAll()
                .on(car.getSeatCount(), new CarSeatCountValidator())
                .on(car, new InnerValidatorWithoutSharedContext())
                .failOver()
                .doValidate().result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrors(), notNullValue());

        // Sadly only get one error, the InnerValidatorWithoutSharedContext doesn't add error to result
        assertThat(ret.getErrors().size(), is(1));
    }

    @Test
    public void testPassContextToInnerValidatorWithSharedContext() {
        Car car = getValidCar();
        car.setSeatCount(0);

        Result ret = FluentValidator.checkAll()
                .on(car.getSeatCount(), new CarSeatCountValidator())
                .on(car, new InnerValidatorWithSharedContext())
                .failOver()
                .doValidate().result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrors(), notNullValue());
        assertThat(ret.getErrors().size(), is(2));
        assertThat(ret.getErrors().get(1), is("inner error of seat count"));
    }

    class InnerValidatorWithSharedContext extends ValidatorHandler<Car> implements Validator<Car> {

        @Override
        public boolean validate(ValidatorContext context, final Car car) {
            // Pass the context to QuickValidator to create a new FluentValidator instance,
            // so that validation result can be set within the same context,
            // or you can consider the context to be shared.
            return QuickValidator.doAndGetComplexResult2(new Decorator() {
                @Override
                public FluentValidator decorate(FluentValidator fv) {
                    return fv.on(car, new Validator<Car>() {
                        @Override
                        public boolean accept(ValidatorContext context, Car car) {
                            return true;
                        }

                        @Override
                        public boolean validate(ValidatorContext context, Car car) {
                            if (car.getSeatCount() == 0) {
                                context.addErrorMsg("inner error of seat count");
                                return false;
                            }
                            return true;
                        }

                        @Override
                        public void onException(Exception e, ValidatorContext context, Car car) {

                        }

						@Override
						public boolean validate(ValidatorContext context, Car t, String message) {
							// TODO Auto-generated method stub
							return false;
						}
                    });
                }
            }, context).isSuccess();
        }
    }

    class InnerValidatorWithoutSharedContext extends ValidatorHandler<Car> implements Validator<Car> {

        @Override
        public boolean validate(ValidatorContext context, final Car car) {
            // Pass the context to QuickValidator to create a new FluentValidator instance,
            // so that validation result can be set within the same context,
            // or you can consider the context to be shared.
            return QuickValidator.doAndGetComplexResult(new Decorator() {
                @Override
                public FluentValidator decorate(FluentValidator fv) {
                    return fv.on(car, new Validator<Car>() {
                        @Override
                        public boolean accept(ValidatorContext context, Car car) {
                            return true;
                        }

                        @Override
                        public boolean validate(ValidatorContext context, Car car) {
                            if (car.getSeatCount() == 0) {
                                context.addErrorMsg("inner error of seat count");
                                return false;
                            }
                            return true;
                        }

                        @Override
                        public void onException(Exception e, ValidatorContext context, Car car) {

                        }

						@Override
						public boolean validate(ValidatorContext context, Car t, String message) {
							// TODO Auto-generated method stub
							return false;
						}
                    });
                }
            }).isSuccess();
        }
    }

    private Car getValidCar() {
        return new Car("BMW", "LA1234", 5);
    }

}
