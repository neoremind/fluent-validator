package com.baidu.unbiz.fluentvalidator.dto;

import java.util.List;

import com.baidu.unbiz.fluentvalidator.annotation.FluentValid;
import com.baidu.unbiz.fluentvalidator.annotation.FluentValidate;
import com.baidu.unbiz.fluentvalidator.validator.StringValidator;

/**
 * @author zhangxu
 */
public class Garage {

    @FluentValidate({StringValidator.class})
    private String str;

    @FluentValid
    private Car singleCar;

    @FluentValid
    private List<Car> collectionCar;

    @FluentValid
    private Car[] arrayCar;

    public Car[] getArrayCar() {
        return arrayCar;
    }

    public void setArrayCar(Car[] arrayCar) {
        this.arrayCar = arrayCar;
    }

    public List<Car> getCollectionCar() {
        return collectionCar;
    }

    public void setCollectionCar(List<Car> collectionCar) {
        this.collectionCar = collectionCar;
    }

    public Car getSingleCar() {
        return singleCar;
    }

    public void setSingleCar(Car singleCar) {
        this.singleCar = singleCar;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
