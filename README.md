# Fluent Validator
![](https://travis-ci.org/neoremind/fluent-validator.svg?branch=master)
![](https://coveralls.io/repos/neoremind/fluent-validator/badge.svg?branch=master&service=github)

Validating data is a common task that occurs throughout any application, especially the business logic layer. As for some quite complex scenarios, often the same or similar validation logic is scattered everywhere, thus it is hard to reuse. 

To avoid duplication and do validation related work as easy as possible, *Fluent Validator* provides the power to support validations with ease by leveraging the [fluent interface]() style and [JSR 303 - Bean Validation]() specification, and here we choose [Hibernate Validator]() which is the best implementation of this JSR.


## 1. Quick Start
This chapter will show you how to get started with Fluent Validator.

### 1.1 Prerequisite

Add the below dependency to pom.xml for a maven enabled project. There are no other dependencies for fluent validator, that means your project will not be overwhelmed by other unwanted libraries. 

	<dependency>
    	<groupId>com.baidu.beidou</groupId>
    	<artifactId>fluent-validator</artifactId>
    	<version>1.0.0-SNAPSHOT</version>
	</dependency>


### 1.2 Create a domain model

Create a domain model or you can call it entity to be validated on later.

    public class Car {
        private String manufacturer;
        private String licensePlate;
        private int seatCount;
        
        // getter and setter...
    }



### 1.3 Applying constraints
First off, get a valid Car instance and then use Fluent Validator to bind each of the license plate, manufacturer and seat count to some self implemented validators. At last, validating shall be applied to the fields of a Car instance in the sequence that they are added and a *Result* is returned which might contains the error messages.

    Car car = getValidCar();

    Result ret = FluentValidator.checkAll()
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator())
                .doValidate();
                
    System.out.println(ret);


Let's take a look into one the these self implemented validators - CarSeatCountValidator. 

```
public class CarSeatCountValidator extends ValidatorHandler<Integer> implements Validator<Integer> {

    @Override
    public boolean validate(ValidatorContext context, Integer t) {
        if (t < 2) {
            context.addErrorMsg(String.format("Seat count is not valid, invalid value=%s", t));
            return false;
        }
        return true;
    }
}
```
To perform a validation of constraint, we have the class extending ValidatorHandler class and implementing Validator interface. CarSeatCountValidator is able to do validation on an primitive *int* value. 

If the seat count is less than two, it will return false and an error message is put into the context. If fail fast strategy is enbled, the result output would be:

    Result{hasError=true, errorMsgs=[Seat count is not valid, invalid value=99]}


If the seat count validates successfully, it will return true. So that the process goes to the next validator. It every fileds do not violate any constraint, the result output would be:

    Result{hasError=false, errorMsgs=null}

More information about some other cool features of fluent validator, please go next. You can continue exploring from the next chapter.


## 2. Basic validation step by step

The FluentValiator is the main entry point to perform validation. FluentValiator is inspired by [Fluent Interface](http://www.martinfowler.com/bliki/FluentInterface.html) which defined an inner-DSL within Java language for programers to use. A fluent interface implies that its primary goal is to make it easy to SPEAK and UNDERSTAND. And that is what FluentValiator tries to do.


### 2.1 Obtain an FluentValidator instance
The first step towards validating an entity instance is to get hold of a FluentValidator instance. The only way is to use the static FluentValidator.checkAll() method:

    FluentValidator.checkAll();

Afterwards we will learn how to use the different methods of the FluentValidator class.


### 2.2 Validate value or instance

The FluentValidator class use on method to either validate entire entities or just a single properties of the entity. You can add as much targets and its speicifed validators as possible.

The following shows validating on some properties of Car instance.

    FluentValidator.checkAll()
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator());

The following shows validating on Car instance.

    FluentValidator.checkAll()
                .on(car, new CarValidator());

Note that when checking all on something, it will not perform executing util doValidate method is called. So you can think of on method as some preparations.

### 2.3 Create Validator

Create validator by implementing Validator interface. 

    public interface Validator<T> {

        boolean accept(ValidatorContext context, T t);

        boolean validate(ValidatorContext context, T t);
    
        void onException(Exception e, ValidatorContext context, T t);

    }

Accept method is where you can determine whether to perform validation on the target, thus if false is returned, the validate method will not be called. 

Validate method is where the main validation work stays. Returning true so that FluentValidator will go on to the next validator if there are any left. Returning false will probably stop the validation process only if fail fast strategy is enable. You can learn more about fail fast and fail over on the next episode.

OnException method gives you the power to do some callback work whenever an exception is throwed on accept and validate method.

Note that if you do not want to implement all the methods for your validator, you can have your validator extends ValidatorHandler, like below:

    public class CarValidator extends ValidatorHandler<Car> implements Validator<Car> {
        //...
    }


### 2.4 Fail fast or fail over
Use failFast() to prevent the processing from continuing if any validator returns false on validate method.

    FluentValidator.checkAll().failFast()
                .on(car.getManufacturer(), new CarManufacturerValidator())


Use failOver() to ignore the failures so that all the valiators will be performed.

    FluentValidator.checkAll().failOver()
                .on(car.getManufacturer(), new CarManufacturerValidator())
 

### 2.5 On what condtion to do validate

Use when() on a specified regular expression to determine whether to do validation against the target. Note that the working scope is not applied to the preview valiator.

    FluentValidator.checkAll().failOver()
                .on(car.getManufacturer(), new CarManufacturerValidator()).when(a == b)

### 2.6 Do validation
Once doValidate() method is called, it means to perform validation of all constraints of a given entity instance. This is where all the validators is really executed. And a result is returned which contains error messages if have.

    Result ret = FluentValidator.checkAll()
                .on(car.getManufacturer(), new CarManufacturerValidator()).when(true)
                .doValidate();

### 2.7 Advance features

#### 2.7.1 ValidatorChain

In addition to validator, applying multiple constraints of the same instance or value is supported. You can wrap all the validators in one ValidatorChain. This is very useful when it comes to reuse some of the basic validators and composite them together to build one chain. Espeically if you are using [Spring](http://spring.io) framework, you will find it a good way to maintain the chain within the container.

    ValidatorChain chain = new ValidatorChain();
    List<Validator> validators = new ArrayList<Validator>();
    validators.add(new CarValidator());
    chain.setValidators(validators);

    Result ret = FluentValidator.checkAll().on(car, chain).doValidate();
    

#### 2.7.2 putAttribute2Context
Use putAttribute2Context(), it allows you to inject some of the properties to the validator from where the validation is performed.

For example, you can put ignoreManufacturer as true in the context and get the use by invoking context.getAttribute(key, class type) within any validator. 

    FluentValidator.checkAll()
                .putAttribute2Context("ignoreManufacturer", true)
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .doValidate();

```
public class CarManufacturerValidator extends ValidatorHandler<String> implements Validator<String> {

    private ManufacturerService manufacturerService = new ManufacturerServiceImpl();

    @Override
    public boolean validate(ValidatorContext context, String t) {
        Boolean ignoreManufacturer = context.getAttribute("ignoreManufacturer", Boolean.class);
        if (ignoreManufacturer != null && ignoreManufacturer) {
            return true;
        }
        // ...
    }

}
```


#### 2.7.3 putClosure2Context
Use putClosure2Context(), it offers the clousre functionality. The classic situation is when you want to obtain an instance or value where it is get from within the validator in a really time-consuming or hard way. And you do not want to waste the time and any logic to get it again.

Below is an example of reuse the allManufacturers that is get from the vaidator.

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

    ValidatorChain chain = new ValidatorChain();
    List<Validator> validators = new ArrayList<Validator>();
    validators.add(new CarValidator());
    chain.setValidators(validators);

    Result ret = FluentValidator.checkAll()
            .putClosure2Context("manufacturerClosure", closure)
            .on(car, chain)
            .doValidate();

    System.out.println(closure.getResult());

        
```
public class CarValidator extends ValidatorHandler<Car> implements Validator<Car> {

    @Override
    public boolean validate(ValidatorContext context, Car car) {
        Closure<List<String>> closure = context.getClosure("manufacturerClosure");
        List<String> manufacturers = closure.executeAndGetResult();

        if (!manufacturers.contains(car.getManufacturer())) {
            context.addErrorMsg(String.format(CarError.MANUFACTURER_ERROR.msg(), car.getManufacturer()));
            return false;
        }

        return FluentValidator.checkAll()
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator())
                .doValidate().hasError();
    }

}
```

For example, you can put ignoreManufacturer as true in the context and get the use by invoking context.getAttribute(key, class type) within any validator.


#### 2.7.4 ValidateCallback

Last but not least, a call back be placed on the doValidate() method like below:

    Result ret = FluentValidator.checkAll().failOver()
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator())
                .doValidate(new DefaulValidateCallback() {
                    @Override
                    public void onFail(ValidatorElementList chained, List<String> errorMsgs) {
                        throw new CustomException("ERROR HERE");
                    }
                });
        
ValidateCallback is the argument interface that doValidate() method needs. onSuccess() method is called when everything goes well, onFail() is called when there is any failures occurrs, onUncaughtException() method is called once there is uncaught exception throwing during the valiation process.

    public interface ValidateCallback {

        void onSuccess(ValidatorElementList validatorElementList);

        void onFail(ValidatorElementList validatorElementList, List<String> errorMsgs);

        void onUncaughtException(Validator validator, Exception e, Object target) throws Exception;

    }
    
If you do not want to implement every method of the interface, you can simply use default DefaulValidateCallback just like the above example code.

#### 2.7.5 RuntimeValidateException

Note that a RuntimeValidateException will be thrown out containing the cause exception from the doValidate() method. You need to try-cath or handle it on your own.






Hibernate Validator also offers a custom, non standardized annotation - org.hibernate.validator.group.GroupSequenceProvider - which allows for dynamic redefinition of the default group sequence. Using the rental car scenario again, one could
￼￼￼21
￼Chapter 2. Validation step by...
￼￼dynamically add the driver checks depending on whether the car is rented or not


Use the validate() method to perform validation of all constraints of a given entity instance

should accept expression/ closure and then populate the semantic model by executing the 

Applying multiple constraints of the same type

validates that its target matches a specified regular expression

Last but not least, a constraint can also be placed on class level. 

Field-level constraints



## JSR 303-Bean Validation support

We will first show how to obtain an Validator instance.


If you are wondering what fluent interface, the please spend some time here before proceeding further.

The same constraint type can belong to different groups and have spe- cific error messages depending on the targeted group.

A Validator instance is thread-safe and may be reused multiple times. For this reason we store it as field of our test class. We can use the Validator now to validate the different car instances in the test methods.



### Dependencies
Navi-pbrpc tries to leverage minimum amount of dependency libraries, so that project built upon Navi-pbrpc will not be overwhelmed by other unwanted libraries. The following are the dependencies.


    [INFO] +- commons-pool:commons-pool:jar:1.5.7:compile
    [INFO] +- com.google.protobuf:protobuf-java:jar:2.5.0:compile
    [INFO] +- io.netty:netty-all:jar:4.0.28.Final:compile
    [INFO] +- org.javassist:javassist:jar:3.18.1-GA:compile
    [INFO] +- org.slf4j:slf4j-api:jar:1.7.7:compile
    [INFO] +- org.slf4j:slf4j-log4j12:jar:1.7.7:compile
    [INFO] |  \- log4j:log4j:jar:1.2.17:compile


### Supports 

![](http://neoremind.net/imgs/gmail.png)
