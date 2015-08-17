package com.baidu.unbiz.fluentvalidator.demo.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

/**
 * @author zhangxu
 */
public class Garage {

    @NotNull
    private Integer garageId;

    @NotNull
    @Pattern(regexp = "[0-9a-zA-Z]+")
    @Length(message = "length is not valid", min = 5)
    private String name;

    @Valid
    private Owner owner;

    private List<Car> carList;

    @Override
    public String toString() {
        return "Garage{" +
                "carList=" + carList +
                ", garageId=" + garageId +
                ", name='" + name + '\'' +
                ", owner=" + owner +
                '}';
    }

    public List<Car> getCarList() {
        return carList;
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }

    public Integer getGarageId() {
        return garageId;
    }

    public void setGarageId(Integer garageId) {
        this.garageId = garageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
