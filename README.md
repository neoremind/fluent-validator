# Fluent Validator
[![Build Status](https://travis-ci.org/neoremind/fluent-validator.svg?branch=master)](https://travis-ci.org/neoremind/fluent-validator)
![](https://coveralls.io/repos/neoremind/fluent-validator/badge.svg?branch=master&service=github)
![](https://maven-badges.herokuapp.com/maven-central/com.baidu.unbiz/fluent-validator/badge.svg)
[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0)


Validating data is a common task that occurs throughout any application, especially the business logic layer. As for some quite complex scenarios, often the same or similar validations are scattered everywhere, thus it is hard to reuse code and break the [DRY](https://en.wikipedia.org/wiki/Don%27t_repeat_yourself) rule. 

To avoid duplication and do validations as easy as possible, **Fluent Validator** provides the power to support validations with ease by leveraging the [fluent interface](https://en.wikipedia.org/wiki/Fluent_interface) style and [JSR 303 - Bean Validation](http://beanvalidation.org/1.0/spec/) specification, and here we choose [Hibernate Validator](http://hibernate.org/validator/) which probably being the most well known one as the implementation of this JSR.

中文手册[请点这里](http://neoremind.com/2016/02/java%E7%9A%84%E4%B8%9A%E5%8A%A1%E9%80%BB%E8%BE%91%E9%AA%8C%E8%AF%81%E6%A1%86%E6%9E%B6fluent-validator/)。

## 1. Quick Start
This chapter will show you how to get started with Fluent Validator.

### 1.1 Prerequisite

In order to use Fluent Validator within a Maven project, simply add the following dependency to your pom.xml. There are no other dependencies for Fluent Validator, which means other unwanted libraries will not overwhelm your project. 

```
<dependency>
    <groupId>com.baidu.unbiz</groupId>
    <artifactId>fluent-validator</artifactId>
    <version>1.0.9</version>
</dependency>
```

*Check out the lastest version on [Maven Central](https://search.maven.org/#search%7Cga%7C1%7Cfluent-validator).*

By default, *org.slf4j:slf4j-api:jar:1.7.7* and *org.slf4j:slf4j-log4j12:jar:1.7.7* are used as logger.

You can switch to other slf4j implementation such as logback and exclude log4j like below: 

```
<dependency>
    <groupId>com.baidu.unbiz</groupId>
    <artifactId>fluent-validator</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

*Snapshot version is also able to work normally. Please add the
following repository to your repositories if you are using the snapshot version. See more on
[sonatype](https://oss.sonatype.org/content/repositories/snapshots/com/baidu/unbiz/).*

    <!-- Note: add this if you are using SNAPSHOT version -->
    <repositories>
        <repository>
            <id>Sonatype</id>
            <name>Sonatype's repository</name>
            <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

### 1.2 Create a domain model

Create a domain model or you can call it entity to be validated on later. For example, a Car instance is created as below.

    public class Car {
        private String manufacturer;
        private String licensePlate;
        private int seatCount;
        
        // getter and setter...
    }



### 1.3 Applying constraints
First off, get a Car instance and then use `FluentValidator.checkAll()` to get an stateful instance of FluentValidator. Secondly, bind each of the license plate, manufacturer and seat count to some custom implemented validators, validating shall be applied to the fields of a Car instance in particular order that they are added by calling `on()` method. Thirdly, executing an intermediate operation such as `on()` does not actually performa until `doValidate()` is called. At last, produce a simple `Result` containing error messages from the above operations by calling `result(toSimple())`.

    Car car = getCar();

    Result ret = FluentValidator.checkAll()
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator())
                .doValidate().result(toSimple());
                
    System.out.println(ret);


You may find the fluent interface and functional style operations pretty much similar to [Stream API](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html) that JDK8 provides.

Let's take a look into one the custom implemented validators - CarSeatCountValidator. 

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

To perform validation of constraint, we have the class extending `ValidatorHandler` class and implementing `Validator` interface, so that CarSeatCountValidator is able to do validation on a primitive *int* value. 

If the seat count is less than two, it will return false and an error message is put into the context. If *fail fast* strategy is enabled, the result output would be:

    Result{isSuccess=false, errors=[Seat count is not valid, invalid value=99]}


If the seat count validates successfully, it will return true. So that the process goes on to the next validator. If none of the fields violate any constraint, the result output would be:

    Result{isSuccess=true, errors=null}

More information about some other cool features of Fluent Validator, please go next. You can continue exploring from the next chapter.


## 2. Basic validation step by step

Fluent Valiator is inspired by [Fluent Interface](http://www.martinfowler.com/bliki/FluentInterface.html) which defined an inner-DSL within Java language for programmers to use. A fluent interface implies that its primary goal is to make it easy to SPEAK and UNDERSTAND. And that is what FluentValiator is dedicated to do, to provide more readable code for you.


### 2.1 Obtain an FluentValidator instance

The `FluentValiator` is the main entry point to perform validation. The first step towards validating an entity instance is to get hold of a FluentValidator instance. The only way is to use the static `FluentValidator.checkAll()` method:

    FluentValidator.checkAll();

Note that FluentValidator is not thread-safe and stateful. 

Afterwards we will learn how to use the different methods of the FluentValidator class.

### 2.2 Create custom validator

Create validator by implementing `Validator` interface. 

    public interface Validator<T> {
        boolean accept(ValidatorContext context, T t);
        boolean validate(ValidatorContext context, T t);
        void onException(Exception e, ValidatorContext context, T t);
    }

`accept()` method is where you can determine whether to perform validation on the target, thus if false is returned, the validate method will not be called. 

`validate()` method is where the main validation work stays. Returning true so that FluentValidator will go on to the next validator if there are any left. Returning false will probably stop the validation process only if fail fast strategy is enable. You can learn more about fail fast and fail over on the next episode.

`onException()` method gives you the power to do some callback works whenever an exception is threw on accept() or validate() method.

Note that if you do not want to implement all the methods for your validator, you can have custom implemented validator extending `ValidatorHandler` like below:

```
public class CarSeatCountValidator extends ValidatorHandler<Integer> implements Validator<Integer> {

    @Override
    public boolean validate(ValidatorContext context, Integer t) {
        if (t < 2) {
            context.addErrorMsg(String.format("Something is wrong about the car seat count %s!", t));
            return false;
        }
        return true;
    }

}
```

When there are errors, there are two ways to put error information into the context and don't forget to return false at last.

Simple way to handle error messages:

    context.addErrorMsg("Something is wrong about the car seat count!");
    return false;
    
More recommended way to put error information into the context would be:

    context.addError(ValidationError.create("Something is wrong about the car seat count!").setErrorCode(100).setField("seatCount").setInvalidValue(t));
    return false;
    

### 2.3 Validate on fields or instances

Validation operations are divided into *intermediate operations* and *terminal operation*, and are combined to form something like fluent interface style or pipelines. 

Intermediate operations are always [lazy](https://en.wikipedia.org/wiki/Lazy_evaluation), executing an intermediate
operation such as `on()` or `onEach()` does not actually perform any validation until terminal operation `doValidate
()` is called.

Terminal operation, such as `doValidate()` or `toResult()` may do real validation or produce a result. After the terminal operation is performed, the validation work is considered done.

The `FluentValidator` uses `on()` or `onEach()` method to either validate entire entities or just some properties of
the entity. You can add as many targets and its specified validators as possible.

The following shows validating on some properties of Car instance.

    FluentValidator.checkAll()
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator());

The following shows validating on Car entity.

    FluentValidator.checkAll()
                .on(car, new CarValidator());

When applying constraints on an Iterable type argument, FluentValidator will validate each element. The following shows validating on a collection of Car entity, each of the elements will be validated.

    FluentValidator.checkAll()
                .onEach(Lists.newArrayList(new Car(), new Car()), new CarValidator());
                
The following shows validating on an array of Car entity.

    FluentValidator.checkAll()
                .onEach(new Car[]{}, new CarValidator());


### 2.4 Fail fast or fail over
Use `failFast()` method to prevent the following validators from getting validated if any validator fails and returns false on `doValidate()` method. 

    FluentValidator.checkAll().failFast()
                .on(car.getManufacturer(), new CarManufacturerValidator())


Use `failOver()` method to ignore the failures so that all the validators will work in order.

    FluentValidator.checkAll().failOver()
                .on(car.getManufacturer(), new CarManufacturerValidator())
 

### 2.5 On what condition to do validate

Use `when()` method on a specified regular expression to determine whether to do validation against the target or not. Note that the working scope is only applied to the previous *Validator* or *ValidatorChain* added.

    FluentValidator.checkAll().failOver()
                .on(car.getManufacturer(), new CarManufacturerValidator()).when(a == b)

### 2.6 Execute validation
Once `doValidate()` method is called, it means to perform validation of all constraints of the given entities or fields. This is where all the validators are really executed. Actaully, ou can do some callback work as well, how-to will be introduced in the Advanced features section.

    Result ret = FluentValidator.checkAll()
                .on(car.getManufacturer(), new CarManufacturerValidator()).when(true)
                .doValidate();


### 2.7 Get result

In almost all cases, terminal operations such as `result()` are eager, because we need to know what happens after the whole sequential operations.

If getting simple error messages can fit your situation, you can simply extract result like below.
```
Result ret = FluentValidator.checkAll()
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator()).failFast()
                .doValidate().result(toSimple());
```

There are some helpful methods you can use on the validation result.

`isSuccess()`,`getErrorMsgs()`,`getErrorNumber()`.

The following shows getting a more complex result which not only contains error messages but also enable you to know the field, the error code and the invalid value if you added them to the context.

```
ComplexResult ret = FluentValidator.checkAll().failOver()
                .on(company, new CompanyCustomValidator())
                .doValidate().result(toComplex());
```

For example, ComplexResult output would be:

```
Result{isSuccess=false, errors=[ValidationError{errorCode=101, errorMsg='{departmentList} may not be null', field='departmentList', invalidValue=null}, ValidationError{errorCode=99, errorMsg='Company id is not valid, invalid value=-1', field='id', invalidValue=8}], timeElapsed(ms)=164}
```

`toSimple()` and `toComplex()` are static methods in `com.baidu.unbiz.fluentvalidator.ResultCollectors`.

### 2.8 Advanced features

#### 2.8.1 ValidatorChain

In addition to `Validator`, applying multiple constraints of the same instance or value is supported. You can wrap all the validators in one `ValidatorChain`. This is very useful when it comes to reuse some of the basic validators and composite them together to build one chain. Especially if you are using [Spring](http://spring.io) framework, you will find it pretty easy to maintain the chain within the container.

    ValidatorChain chain = new ValidatorChain();
    List<Validator> validators = new ArrayList<Validator>();
    validators.add(new CarValidator());
    chain.setValidators(validators);

    Result ret = FluentValidator.checkAll().on(car, chain).doValidate().result(toSimple());
    

#### 2.8.2 Annotation-based validation

Constraints can be expressed by annotating a field of a class of `@FluentValidate` which takes multiple classes implementing `Validator` interface as value. The following shows a field level configuration example:

```
public class Car {

    @FluentValidate({CarManufacturerValidator.class})
    private String manufacturer;

    @FluentValidate({CarLicensePlateValidator.class})
    private String licensePlate;

    @FluentValidate({CarSeatCountValidator.class})
    private int seatCount;

    // getter and setter ...    
}
```

When using field level constraints, there must have getter methods for each of the annotated field.

Next, you can use the method `configure(new SimpleRegistry())` which will allow you to configure where to lookup the annotated validators for FluentValidator instance. By default `SimpleRegistry` is configured well, which means no need to configure it beforehand.   

```
    Car car = getCar();

    Result ret = FluentValidator.checkAll().configure(new SimpleRegistry())
                .on(car)
                .doValidate()
                .result(toSimple());
```

Notice that you can use `onEach()` to validate through an array or a collection.

#### 2.8.3 Groups validation

Groups allow you to restrict the set of constraints applied during validation. 

For example, the class *Add* is in the groups of `@FluentValidate`.

```
public class Car {

    @FluentValidate(value = {CarManufacturerValidator.class}, groups = {Add.class})
    private String manufacturer;

    @FluentValidate({CarLicensePlateValidator.class})
    private String licensePlate;

    @FluentValidate({CarSeatCountValidator.class})
    private int seatCount;
    
}
```

Then when applying constraints like below, only manufacturer field is validated and the other two fields are skipped.

```
Result ret = FluentValidator.checkAll(new Class<?>[] {Add.class})
                .on(car)
                .doValidate()
                .result(toSimple());
```

When applying constraints like before with no parameters in `checkAll()` method, all constraints are applied on Car class.


#### 2.8.4 Cascade validation

FluentValidator not only allows you to validate single class instances but also complete cascaded validation (object graphs). To do so, just annotate a field or property representing a reference to another object with `@FluentValid` as demonstrated below.

```
public class Garage {

    @FluentValidate({CarNotExceedLimitValidator.class})
    @FluentValid
    private List<Car> carList;
}
```

The referenced *List<Car>* object will be validated as well, as the *carList* field is annotated with `@FluentValid`. Note that cascade validation also works for collection-typed fields. That means each contained element can be validated. Also, `@FluentValid` and `@FluentValidate` can work well together.

Cascade validation is recursive, i.e. if a reference marked for cascaded validation points to an object which itself has properties annotated with `@FluentValid`, these references will be followed up by the validation engine as well. 

Note that the validation engine currently will NOT ensure that no infinite loops occur during cascaded validation, for example if two objects hold references to each other.


#### 2.8.5 putAttribute2Context
Use `putAttribute2Context()` method, it allows you to inject some of the properties to the validator or validator chain from the caller - where validations are performed.

For example, you can put *ignoreManufacturer* as true in the context and get the value by invoking `context.getAttribute(key, class type)` within any validator. 

    FluentValidator.checkAll()
                .putAttribute2Context("ignoreManufacturer", true)
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .doValidate().result(toSimple());

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


#### 2.8.6 putClosure2Context
Use `putClosure2Context()` method, it offers the closure functionality. In some situations, when the caller wants to obtain an instance or value where the invocation is delegated down to the validator to do real call later which can be a time-consuming and complex job, and you do not want to waste any time or any logic code to set it again from the caller, it is the best place to use putClosure2Context().

Below is an example of reusing the *allManufacturers* that is set by invoking `closure.executeAndGetResult()` method within the validator, notice that `manufacturerService.getAllManufacturers()` may perform rpc call. And the caller can get the result by simply invoking `closure.getResult()` method.

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
            .doValidate().result(toSimple());

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
                .doValidate().result(toSimple()).isSuccess();
    }

}
```


#### 2.8.7 ValidateCallback

So far we have been ignoring the optional argument `ValidateCallback` that `doValidate()` method takes, but it is time to have a closer look. A callback can be placed on the `doValidate()` method like below:

    Result ret = FluentValidator.checkAll().failOver()
                .on(car.getLicensePlate(), new CarLicensePlateValidator())
                .on(car.getManufacturer(), new CarManufacturerValidator())
                .on(car.getSeatCount(), new CarSeatCountValidator())
                .doValidate(new DefaulValidateCallback() {
                    @Override
                    public void onFail(ValidatorElementList chained, List<String> errorMsgs) {
                        throw new CustomException("ERROR HERE");
                    }
                }).result(toSimple());
 

`onSuccess()` method is called when everything goes well. 

`onFail()` is called when there are failures occurs.

`onUncaughtException()` method is called once there is uncaught exception throwing during the validation process.

    public interface ValidateCallback {

        void onSuccess(ValidatorElementList validatorElementList);

        void onFail(ValidatorElementList validatorElementList, List<String> errorMsgs);

        void onUncaughtException(Validator validator, Exception e, Object target) throws Exception;

    }
    
If you do not want to implement every method of the interface, you can simply use `DefaulValidateCallback` just like the above example and implement methods selectively.

#### 2.8.8 RuntimeValidateException

Last but not least, if there is any exception that is not handled, a RuntimeValidateException will be threw out containing the root cause exception from the `doValidate()` method. 

If there is any exception that re-throw from `onException()` or `onUncaughtException()` method, a RuntimeValidateException wrapping the new cause will be threw out.

You can try-catch or handle it with Spring AOP feature on your own.



## 3. JSR 303 - Bean Validation support

JSR 303 - Bean Validation defines a metadata model and API for entity validation and Hibernate Validator is the best-known implementation.

If you are wondering what JSR 303 - Bean Validation specification is, please spend some time [here](http://beanvalidation.org/1.0/spec/) before proceeding further. And get some knowledge background of what Hibernate Validator is via [this link](http://hibernate.org/validator/).

The following paragraphs will guide you through the steps required to integrate Hibernate Validator with Fluent Validator.

### 3.1 Prerequisite
Add the following dependency to your pom.xml. 

```
<dependency>
    <groupId>com.baidu.unbiz</groupId>
    <artifactId>fluent-validator-jsr303</artifactId>
    <version>1.0.9</version>
</dependency>
```

By default, the following dependencies are what fluent-validator-jsr303 will bring into your project. 

```	
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
    <groupId>com.baidu.unbiz</groupId>
    <artifactId>fluent-validator-jsr303</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```
		

### 3.2 Validate by using Hibernate Validator

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
                .on(company, new HibernateSupportedValidator<Company>().setHibernateValidator(validator))
                .on(company, new CompanyCustomValidator())
                .doValidate().result(toSimple());
        System.out.println(ret);
```
 
Note that HibernateSupportedValidator should first have `javax.validation.Validator` set into its property, otherwise HibernateSupportedValidator will not work normally. The following shows getting Hibernate implemented version of `javax.validation.Validator` and set it into HibernateSupportedValidator. If you use Spring framework, there are certainly quite a few ways to inject javax.validation.Validator, and how-to is omitted here.

    Locale.setDefault(Locale.ENGLISH); // speicify language
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    javax.validation.Validator validator = factory.getValidator();

For example, when Company name is invalid, the result would be:

    Result{isSuccess=false, errors=[{name} must match "[0-9a-zA-Z\4e00-\u9fa5]+"]}

Also HibernateSupportedValidator works well with other custom validators, you can add validators through `on()` as much as you want like below:

    Result ret = FluentValidator.checkAll()
                .on(company, new HibernateSupportedValidator<Company>().setHibernateValidator(validator))
                .on(company, new CompanyCustomValidator())
                .doValidate().result(toSimple());

### 3.3 Use group and group sequence

In case if one wants to do annotation-based validation by using [Grouping constraints](http://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#chapter-groups), FluentValidator is also capable, `checkAll()` method takes a var-arg argument groups.

In the above example of Company class, since no group is specified for any annotation, the default group javax.validation.groups.Default is assumed.

If a ceo property is added to Company class and specifying the group as AddCompany.class which you can define as an interface:

    @Length(message = "Company CEO is not valid", min = 10, groups = {AddCompany.class})
    private String ceo;
    
When using `FluentValidator.checkAll()`, ceo will not be validated at all. Only when AddCompany.class acts as one member of the var-arg argument that `FluentValidator.checkAll()` accepts, the @Length will work but the other default constraints will not be working.

Below is an example if one just needs to validate the ceo property.

    Result ret = FluentValidator.checkAll(AddCompany.class)
                .on(company, new HibernateSupportedValidator<Company>().setHibernateValidator(validator))
                .on(company, new CompanyCustomValidator())
                .doValidate().result(toSimple());

Below is another example if one needs to validate both the ceo property and other default annotation-based properties. By default, constraints are evaluated in no particular order, regardless of which groups they belong to.

    Result ret = FluentValidator.checkAll(Default.class, AddCompany.class)
                .on(company, new HibernateSupportedValidator<Company>().setHibernateValidator(validator))
                .on(company, new CompanyCustomValidator())
                .doValidate().result(toSimple());

If you would like to sepecify the validation order you just need to define an interface and annotate it with `@GroupSequence` like below. So contraints will applied on AddCompany.class first and other properties next. Note that if at least one constraint fails in a sequenced group, none of the constraints of the following groups in the sequence get validated.

    @GroupSequence({AddCompany.class, Default.class})
        public interface GroupingCheck {
    }


## 4. Spring support

### 4.1 Use Spring IoC container to locate validators

As constraints can be expressed by annotating a field of a class of `@FluentValidate` which takes multiple classes implementing `Validator` interface as value. In the above chapter, `SimpleRegistry` helps to locate and create the validators by simply calling `newInstance()` method if class exists within the current class loader. You can make an explicit choice about which implementation to use. One alternative is to lookup instance from Spring IoC container by using `SpringApplicationContextRegistry`, that enables you to build some powerful validators such that maybe they rely on dependency beans like `JdbcTemplate`.

To use `SpringApplicationContextRegistry`, add the following dependency to your pom.xml. 

```
<dependency>
    <groupId>com.baidu.unbiz</groupId>
    <artifactId>fluent-validator-spring</artifactId>
    <version>1.0.9</version>
</dependency>
```

By default, the following dependencies are what fluent-validator-spring will bring into your project. 

```	
[INFO] +- org.springframework:spring-context-support:jar:4.1.6.RELEASE:compile
[INFO] |  +- org.springframework:spring-beans:jar:4.1.6.RELEASE:compile
[INFO] |  +- org.springframework:spring-context:jar:4.1.6.RELEASE:compile
[INFO] |  |  +- org.springframework:spring-aop:jar:4.1.6.RELEASE:compile
[INFO] |  |  |  \- aopalliance:aopalliance:jar:1.0:compile
[INFO] |  |  \- org.springframework:spring-expression:jar:4.1.6.RELEASE:compile
[INFO] |  \- org.springframework:spring-core:jar:4.1.6.RELEASE:compile
[INFO] +- org.slf4j:slf4j-api:jar:1.7.7:compile
[INFO] +- org.slf4j:slf4j-log4j12:jar:1.7.7:compile
[INFO] |  \- log4j:log4j:jar:1.2.17:compile
```

You can exclude any of them but be sure to have *fluent-validator* left by configuring like below: 

```
<dependency>
    <groupId>com.baidu.unbiz</groupId>
    <artifactId>fluent-validator-spring</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context-support</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

The usage is shown as below.

```
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
```

### 4.2 Use FluentValidateInterceptor 
FluentValidator provides you an useful interceptor to work with your existing Spring framework supported project, so that `FluentValidator` API is no longer needed to perform validation explicitly. What you should do is to focusing on the core business logic stuff and leave the validation work to FluentValidator by using `@FluentValid` annotation on the speicific argument. Examples are shown below:

```
@Service
public class GarageServiceImpl implements GarageService {

    @Override
    public List<Car> addCars(@FluentValid List<Car> cars) {
        // do biz here...
        return cars;
    }
}
```

Spring configuration goes like below, it has *GarageServiceImpl* intercepted with *fluentValidateInterceptor* and *fluentValidateInterceptor* has a callback that handles what to do when there is failure or uncaught exception threw.

    <bean id="validateCarCallback" class="com.baidu.unbiz.fluentvalidator.demo.callback.ValidateCarCallback"/>

    <bean id="fluentValidateInterceptor"
          class="com.baidu.unbiz.fluentvalidator.interceptor.FluentValidateInterceptor">
        <property name="callback" ref="validateCarCallback"/>
    </bean>

    <bean class="org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator">
        <property name="beanNames">
            <list>
                <value>*ServiceImpl</value>
            </list>
        </property>
        <property name="interceptorNames">
            <list>
                <value>fluentValidateInterceptor</value>
            </list>
        </property>
    </bean>


*ValidateCarCallback* implementation is shown as below: 

```
public class ValidateCarCallback extends DefaulValidateCallback implements ValidateCallback {

    @Override
    public void onFail(ValidatorElementList validatorElementList, List<ValidationError> errors) {
        throw new CarException(errors.get(0).getErrorMsg());
    }

    @Override
    public void onSuccess(ValidatorElementList validatorElementList) {
        System.out.println("Everything works fine!");
    }

    @Override
    public void onUncaughtException(Validator validator, Exception e, Object target) throws Exception {
        throw new CarException(e);
    }
}
```

Test case is shown as below:

    @Test
    public void testAddCarsFail() {
        try {
            List<Car> cars = getValidCars();
            cars.get(0).setLicensePlate("xxx123"); // fail fast
            garageService.addCars(cars);
        } catch (CarException e) {
            assertThat(e.getClass().getName(), is(CarException.class.getName()));
            assertThat(e.getMessage(), is(String.format(CarError.LICENSEPLATE_ERROR.msg(), "xxx123")));
        }
    }

Moreover, one can configure some add-on validators within the @FluentValid annotation upon specific arguments, example is shown as below as NotEmptyValidator and SizeNotExceedValidator will be performed on cars beforehand, so that one can check if the cars argument violates not null or size limitation, all other validation works will be executed afterwards.
 
    @Override
    public List<Car> addCars(@FluentValid(NotEmptyValidator.class, SizeNotExceedValidator.class) List<Car> cars) {
        // do biz here...
        return cars;
    }


## Examples

All test cases or samples can be found from the below links:

[Samples](https://github.com/neoremind/fluent-validator/blob/master/fluent-validator-demo/src/main/java/com/baidu/unbiz/fluentvalidator/demo/service/impl/GarageServiceImpl.java)

[Spring support Samples](https://github.com/neoremind/fluent-validator/blob/master/fluent-validator-demo/src/main/java/com/baidu/unbiz/fluentvalidator/demo/service/impl/GarageServiceImpl2.java)

[Basic test cases](https://github.com/neoremind/fluent-validator/tree/master/fluent-validator/src/test/java/com/baidu/unbiz/fluentvalidator)

[JSR 303 - Hibernate validator supported test cases](https://github.com/neoremind/fluent-validator/tree/master/fluent-validator-jsr303/src/test/java/com/baidu/unbiz/fluentvalidator/jsr303)

[Spring registry usage](https://github.com/neoremind/fluent-validator/blob/master/fluent-validator-spring/src/test/java/com/baidu/unbiz/fluentvalidator/registry/impl/SpringApplicationContextRegistryTest.java)

## Support

Email: ![](http://neoremind.com/imgs/gmail.png)
