package com.baidu.unbiz.fluentvalidator.dto;

import com.baidu.unbiz.fluentvalidator.annotation.FluentValidate;
import com.baidu.unbiz.fluentvalidator.group.CheckManufacturer;
import com.baidu.unbiz.fluentvalidator.validator.CarLicensePlateValidator;
import com.baidu.unbiz.fluentvalidator.validator.CarManufacturerValidator;
import com.baidu.unbiz.fluentvalidator.validator.CarSeatCountValidator;

/**
 * @author zhangxu
 */
public class Car {

    @FluentValidate(value = {CarManufacturerValidator.class}, groups = {CheckManufacturer.class})
    private String manufacturer;

    @FluentValidate({CarLicensePlateValidator.class})
    private String licensePlate;

    @FluentValidate({CarSeatCountValidator.class})
    private int seatCount;

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
