package com.baidu.unbiz.fluentvalidator.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.baidu.unbiz.fluentvalidator.annotation.FluentValidate;
import com.baidu.unbiz.fluentvalidator.groups.Add;
import com.baidu.unbiz.fluentvalidator.validator.CarLicensePlateValidator;
import com.baidu.unbiz.fluentvalidator.validator.CarManufacturerValidator;
import com.baidu.unbiz.fluentvalidator.validator.CarSeatCountValidator;

/**
 * @author zhangxu
 */
public class Car {

    @FluentValidate({CarManufacturerValidator.class})
    @NotBlank
    private String manufacturer;

    @FluentValidate(value = {CarLicensePlateValidator.class}, groups = {Add.class})
    @NotNull(groups = {Add.class})
    private String licensePlate;

    @FluentValidate({CarSeatCountValidator.class})
    private int seatCount;

    @Override
    public String toString() {
        return "Car{" +
                "licensePlate='" + licensePlate + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", seatCount=" + seatCount +
                '}';
    }

    public Car(String manufacturer, String licencePlate, int seatCount) {
        this.manufacturer = manufacturer;
        this.licensePlate = licencePlate;
        this.seatCount = seatCount;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

}
