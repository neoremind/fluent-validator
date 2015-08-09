# Fluent Validator
![](https://travis-ci.org/neoremind/fluent-validator.svg?branch=master)
![](https://coveralls.io/repos/neoremind/fluent-validator/badge.svg?branch=master&service=github)

Validating data is a common task that occurs throughout any application, especially the business logic layer. As for some quite complex scenarios, often the same or similar validation logic is scattered everywhere, thus it is hard to reuse code. 

To avoid duplication and do validations as easy as possible, **Fluent Validator** provides the power to support validations with ease by leveraging the [fluent interface]() style and [JSR 303 - Bean Validation]() specification, and here we choose [Hibernate Validator]() which probably being the most well known one as the implementation of this JSR .


## 1. Quick Start
This chapter will show you how to get started with Fluent Validator.

### 1.1 Prerequisite

In order to use Fluent Validator within a Maven project, simply add the following dependency to your pom.xml. There are no other dependencies for Fluent Validator, which means other unwanted libraries will not overwhelm your project. 

	<dependency>
    	<groupId>com.baidu.beidou</groupId>
    	<artifactId>fluent-validator</artifactId>
    	<version>1.0.0-SNAPSHOT</version>
	</dependency>


### 1.2 Create a domain model

Create a domain model or you can call it entity to be validated on later. For example, a Car class is created as below.

    public class Car {
        private String manufacturer;
        private String licensePlate;
        private int seatCount;
        
        // getter and setter...
    }



### 1.3 Applying constraints
First, get a valid Car instance and then use `FluentValidator` to bind each of the license plate, manufacturer and seat count to some custom implemented validators. At last, validating shall be applied to the fields of a Car instance in the sequence that they are added by calling `on()` method and a `Result` is returned which contains the error messages.

    Car car = getValidCar();

    Result ret = FluentValidator.checkAll()
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator())
                .doValidate();
                
    System.out.println(ret);


Let's take a look into one the the custom implemented validators - CarSeatCountValidator. 

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

To perform a validation of constraint, we have the class extending `ValidatorHandler` class and implementing `Validator` interface. CarSeatCountValidator is able to do validation on a primitive *int* value. 

If the seat count is less than two, it will return false and an error message is put into the context. If fail fast strategy is enabled, the result output would be:

    Result{hasError=true, errorMsgs=[Seat count is not valid, invalid value=99]}


If the seat count validates successfully, it will return true. So that the process goes to the next validator. If all the fields do not violate any constraint, the result output would be:

    Result{hasError=false, errorMsgs=null}

More information about some other cool features of Fluent Validator, please go next. You can continue exploring from the next chapter.


## 2. Basic validation step by step

Fluent Valiator is inspired by [Fluent Interface](http://www.martinfowler.com/bliki/FluentInterface.html) which defined an inner-DSL within Java language for programmers to use. A fluent interface implies that its primary goal is to make it easy to SPEAK and UNDERSTAND. And that is what Fluent Valiator dedicated to do, to provide more readable code.


### 2.1 Obtain an FluentValidator instance

The `FluentValiator` is the main entry point to perform validation. The first step towards validating an entity instance is to get hold of a FluentValidator instance. The only way is to use the static `FluentValidator.checkAll()` method:

    FluentValidator.checkAll();

Afterwards we will learn how to use the different methods of the FluentValidator class.


### 2.2 Validate on fields or instances

The `FluentValidator` uses `on()` method to either validate entire entities or just a single properties of the entity. You can add as many targets and its specified validators as possible.

The following shows validating on some properties of Car instance.

    FluentValidator.checkAll()
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator());

The following shows validating on Car instance.

    FluentValidator.checkAll()
                .on(car, new CarValidator());

Note that when checking all on something, it will not perform executing until `doValidate()` method is called. So you can think of `on()` method as some preparations.

### 2.3 Create custom validator

Create validator by implementing `Validator` interface. 

    public interface Validator<T> {

        boolean accept(ValidatorContext context, T t);

        boolean validate(ValidatorContext context, T t);
    
        void onException(Exception e, ValidatorContext context, T t);

    }

`accept()` method is where you can determine whether to perform validation on the target, thus if false is returned, the validate method will not be called. 

`validate()` method is where the main validation work stays. Returning true so that FluentValidator will go on to the next validator if there are any left. Returning false will probably stop the validation process only if fail fast strategy is enable. You can learn more about fail fast and fail over on the next episode.

`onException()` method gives you the power to do some callback works whenever an exception is threw on accept() or validate() method.

Note that if you do not want to implement all the methods for your validator, you can have custom validator extending `ValidatorHandler` like below:

    public class CarValidator extends ValidatorHandler<Car> implements Validator<Car> {
        //...
    }


### 2.4 Fail fast or fail over
Use `failFast()` method to prevent the processing from continuing if any validator returns false on validate method.

    FluentValidator.checkAll().failFast()
                .on(car.getManufacturer(), new CarManufacturerValidator())


Use `failOver()` method to ignore the failures so that all the validators will be performed.

    FluentValidator.checkAll().failOver()
                .on(car.getManufacturer(), new CarManufacturerValidator())
 

### 2.5 On what condition to do validate

Use `when()` method on a specified regular expression to determine whether to do validation against the target. Note that the working scope is only applied to the previous validator or validator chain.

    FluentValidator.checkAll().failOver()
                .on(car.getManufacturer(), new CarManufacturerValidator()).when(a == b)

### 2.6 Execute validation
Once `doValidate()` method is called, it means to perform validation of all constraints of the given entity instances or fields. This is where all the validators is really executed. And a result is returned which contains error messages if have.

    Result ret = FluentValidator.checkAll()
                .on(car.getManufacturer(), new CarManufacturerValidator()).when(true)
                .doValidate();

### 2.7 Get result

The following method can be called on `Result` to help you know the validation result.
`getErrorMsgs()`,`hasError()`, `hasNoError()`,`getErrorNumber()`.


### 2.8 Advance features

#### 2.8.1 ValidatorChain

In addition to `Validator`, applying multiple constraints of the same instance or value is supported. You can wrap all the validators in one `ValidatorChain`. This is very useful when it comes to reuse some of the basic validators and composite them together to build one chain. Especially if you are using [Spring](http://spring.io) framework, you will find it a good way to maintain the chain within the container.

    ValidatorChain chain = new ValidatorChain();
    List<Validator> validators = new ArrayList<Validator>();
    validators.add(new CarValidator());
    chain.setValidators(validators);

    Result ret = FluentValidator.checkAll().on(car, chain).doValidate();
    

#### 2.8.2 putAttribute2Context
Use `putAttribute2Context()` method, it allows you to inject some of the properties to the validator or validator chain from where validations are performed.

For example, you can put *ignoreManufacturer* as true in the context and get the value by invoking `context.getAttribute(key, class type)` within any validator. 

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


#### 2.8.3 putClosure2Context
Use `putClosure2Context()` method, it offers the closure functionality. The classic situation is when the caller wants to obtain an instance or value where the invocation is delegated down to the validator to do real call later which can be a time-consuming and complex job. And you do not want to waste any time or any logic code to set it again from the caller.

Below is an example of reuse the *allManufacturers* that is set by invoking `closure.executeAndGetResult()` method within the validator, notice that `manufacturerService.getAllManufacturers()` which may perform rpc call. And the caller of Fluent Validator can get the result by simply invoking `closure.getResult()` method.

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


#### 2.8.4 ValidateCallback

Last but not least, a callback be placed on the doValidate() method like below:

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
        
`ValidateCallback` is an optional argument that `doValidate()` method needs. 

`onSuccess()` method is called when everything goes well. 

`onFail()` is called when there is any failures occurs.

`onUncaughtException()` method is called once there is uncaught exception throwing during the validation process.

    public interface ValidateCallback {

        void onSuccess(ValidatorElementList validatorElementList);

        void onFail(ValidatorElementList validatorElementList, List<String> errorMsgs);

        void onUncaughtException(Validator validator, Exception e, Object target) throws Exception;

    }
    
If you do not want to implement every method of the interface, you can simply use default `DefaulValidateCallback` just like the above  example and implement methods selectively.

#### 2.8.5 RuntimeValidateException

A RuntimeValidateException will be threw out containing the root cause exception from the `doValidate()` method. You can try-catch or handle it on your own.


## 3. JSR 303 - Bean Validation support

If you are wondering what JSR 303 - Bean Validation specification, please spend some time [here](http://beanvalidation.org/1.0/spec/) before proceeding further. And get some knowledge background of what Hibernate Validator is via [this link](http://hibernate.org/validator/).

JSR 303 - Bean Validation defines a metadata model and API for entity validation and Hibernate Validator is the best-known implementation.

The following paragraphs will guide you through the steps required to integrate Hibernate Validator with Fluent Validator.

### 3.1 Prerequisite
Add the following dependency to your pom.xml. 

	<dependency>
    	<groupId>com.baidu.beidou</groupId>
    	<artifactId>fluent-validator-jsr303</artifactId>
    	<version>1.0.0-SNAPSHOT</version>
	</dependency>
	
By default, the following dependencies are what fluent-validator-jsr303 will bring into your project. 

```	
[INFO] +- com.baidu.unbiz:fluent-validator:jar:1.0.0-SNAPSHOT:compile
[INFO] +- org.hibernate:hibernate-validator:jar:5.2.1.Final:compile
[INFO] |  +- javax.validation:validation-api:jar:1.1.0.Final:compile
[INFO] |  +- org.jboss.logging:jboss-logging:jar:3.2.1.Final:compile
[INFO] |  \- com.fasterxml:classmate:jar:1.1.0:compile
[INFO] +- javax.el:javax.el-api:jar:2.2.4:compile
[INFO] +- org.glassfish.web:javax.el:jar:2.2.4:compile
[INFO] +- org.slf4j:slf4j-api:jar:1.7.7:compile
[INFO] +- org.slf4j:slf4j-log4j12:jar:1.7.7:compile
[INFO] |  \- log4j:log4j:jar:1.2.17:compile
```

You can exclude any of them but be sure to have *fluent-validator* left by configuring like below: 

```
<dependency>
	<groupId>com.baidu.beidou</groupId>
	<artifactId>fluent-validator-jsr303</artifactId>
	<exclusions>
		<exclusion>
			<groupId>org.slf4j</groupId>
			<artifactId>slf4j-log4j12</artifactId>
		</exclusion>
	</exclusions>
</dependency>
```
		

### 3.1 Validate by using Hibernate Validator

Since using annotation-based constraints can be an easy way to do validation on an instance. Fluent Validator will definitely leverage the useful feature provided by Hibernate Validator.


Below is an example of applying annotation-based constraints on Car instance. The @NotEmpty, @Pattern, @NotNull, @Size, @Length and @Valid annotations are used to declare the constraints. For more information please see [Hibernate Validator official documentation](http://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/).

```
public class Company {
    @NotEmpty
    @Pattern(regexp = "[0-9a-zA-Z\4e00-\u9fa5]+")
    private String name;

    @NotNull(message = "The establishTime must not be null")
    private Date establishTime;

    @NotNull
    @Size(min = 0, max = 10)
    @Valid
    private List<Department> departmentList;

    // getter and setter...
}

public class Department {
    @NotNull
    private Integer id;

    @Length(max = 30)
    private String name;
    
    // getter and setter...
}
    
```

To perform validation, one can use `FluentValidator` without any problem. Just use the `HibernateSupportedValidator` as one of the validators you want to apply on the target. 

```
Result ret = FluentValidator.checkAll()
                .on(company, new HibernateSupportedValidator<Company>().setValidator(validator))
                .on(company, new CompanyCustomValidator())
                .doValidate();
        System.out.println(ret);
```
 
Note that HibernateSupportedValidator should first have `javax.validation.Validator` set into its specified property, otherwise HibernateSupportedValidator will not work normally. The following shows getting Hibernate implemented version of `javax.validation.Validator` and set it into HibernateSupportedValidator. If you use Spring framework, there are certainly quite a few ways to inject javax.validation.Validator, how-to is omitted here.

    Locale.setDefault(Locale.ENGLISH); // speicify language
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    javax.validation.Validator validator = factory.getValidator();

When Company name is invalid, the result would be:

    Result{hasError=true, errorMsgs=[{name} must match "[0-9a-zA-Z\4e00-\u9fa5]+"]}

Also HibernateSupportedValidator works well with other custom validators, you can add validators through `on()` as much as you want like below:

    Result ret = FluentValidator.checkAll()
                .on(company, new HibernateSupportedValidator<Company>().setValidator(validator))
                .on(company, new CompanyCustomValidator())
                .doValidate();

### 3.2 FluentHibernateValidator can be a replacement

Usually `FluentHibernateValidator` is the same as `FluentValidator`. But in case if one wants to do annotation-based validation by using [Grouping constraints](http://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#chapter-groups), FluentHibernateValidator is here to help. 

For example, you add ceo field to Car class and specifying the group as AddCompany.class which you can define as an interface:

    @Length(message = "Company CEO is not valid", min = 10, groups = {AddCompany.class})
    private String ceo;
    
When using `FluentHibernateValidator.checkAll()` or `FluentValidator.checkAll()`, ceo will not be validated at all. Only when AddCompany.class acts as one member of the array argument that `FluentHibernateValidator.checkAll()` accepts, the @Length will work.

    Result ret = FluentHibernateValidator.checkAll(AddCompany.class)
                .on(company, new HibernateSupportedValidator<Company>().setValidator(validator))
                .on(company, new CompanyCustomValidator())
                .doValidate();


## Supports 

![](http://neoremind.net/imgs/gmail.png)
