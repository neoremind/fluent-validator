package com.baidu.unbiz.fluentvalidator;

import com.baidu.unbiz.fluentvalidator.annotation.FluentValid;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class FluentValidatorWIthoutAnnotationsTest {

    class Passenger {
        String name;
        Boolean hasSeatbeltOn;

        public Passenger(String name, Boolean hasSeatbeltOn) {
            this.name = name;
            this.hasSeatbeltOn = hasSeatbeltOn;
        }
    }

    class Car {
        private String make;
        private List<Passenger> passengers = Lists.newArrayList();

        public void addPassenger(Passenger passenger) {
            passengers.add(passenger);
        }
    }

    class SeatBeltValidator extends ValidatorHandler<Boolean> implements Validator<Boolean> {
        private String name;

        public SeatBeltValidator(String name) {
            this.name = name;
        }

        public boolean validate(ValidatorContext context, Boolean hasSeatbeltOn) {
            if (!hasSeatbeltOn) {
                ValidationError error = new ValidationError();
                error.setErrorCode(123);
                error.setErrorMsg("passenger " + name + " must have their seat belt on");
                context.addError(error);
            }

            return hasSeatbeltOn;
        }
    }

    class SeatBeltValidatorWithoutError extends ValidatorHandler<Boolean> implements Validator<Boolean> {
        public boolean validate(ValidatorContext context, Boolean hasSeatbeltOn) {
            return hasSeatbeltOn;
        }
    }

    class PassengerValidator extends ValidatorHandler<Passenger> implements Validator<Passenger> {
        @Override
        public boolean validate(ValidatorContext context, Passenger passenger) {
            ComplexResult result = FluentValidator
                    .checkAll()
                    .on(passenger.hasSeatbeltOn, new SeatBeltValidator(passenger.name))
                    .on(passenger.hasSeatbeltOn, new SeatBeltValidatorWithoutError())
                    .failOver()
                    .doValidate()
                    .result(ResultCollectors.toComplex());

            for (ValidationError error : result.getErrors()) {
                context.addError(error);
            }

            return result.isSuccess();
        }
    }

    class CarValidator extends ValidatorHandler<Car> implements Validator<Car> {
        @Override
        public boolean validate(ValidatorContext context, Car car) {
            ComplexResult result = FluentValidator
                    .checkAll()
                    .onEach(car.passengers, new PassengerValidator())
                    .failOver()
                    .doValidate()
                    .result(ResultCollectors.toComplex());

            for (ValidationError error : result.getErrors()) {
                context.addError(error);
            }

            return result.isSuccess();
        }
    }

    @Test
    public void itCanValidateComplexObjects() {
        Car car = new Car();
        car.addPassenger(new Passenger("Adam", false));
        car.addPassenger(new Passenger("Joe", true));

        ComplexResult result = FluentValidator
                .checkAll()
                .on(car, new CarValidator())
                .failOver()
                .doValidate()
                .result(ResultCollectors.toComplex());

        assertThat(result.getErrors().size(), equalTo(1));
        assertThat(result.getErrors().get(0).getErrorMsg(), equalTo("passenger Adam must have their seat belt on"));
    }
}
