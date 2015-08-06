# Fluent Validator
![](https://travis-ci.org/neoremind/fluent-validator.svg?branch=master)
![](https://coveralls.io/repos/neoremind/fluent-validator/badge.svg?branch=master&service=github)

Fluent validator demo

```
Result ret = FluentValidator.checkAll().failFast()
            .on(car.getLicensePlate(), new CarLicensePlateValidator())
            .on(car.getManufacturer(), new CarManufacturerValidator())
            .on(car.getSeatCount(), new CarSeatCountValidator())
            .doValidate(new DefaulValidateCallback() {
                @Override
                public void onUncaughtException(Validator validator, Throwable t, Object target)
                        throws Throwable {
                    System.err.println("Fetal here!");
                }
            });
```
                    

```
public class CarManufacturerValidator extends ValidatorHandler<String> implements Validator<String> {

    private ManufacturerService manufacturerService = new ManufacturerServiceImpl();

    @Override
    public boolean validate(ValidatorContext context, String t) {
        Boolean ignoreManufacturer = context.getAttribute("ignoreManufacturer", Boolean.class);
        if (ignoreManufacturer != null && ignoreManufacturer) {
            return true;
        }
        if (!manufacturerService.getAllManufacturers().contains(t)) {
            context.addErrorMsg(String.format(CarError.MANUFACTURER_ERROR.msg(), t));
            return false;
        }
        return true;
    }

}

```