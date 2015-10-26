package com.baidu.unbiz.fluentvalidator.error;

/**
 * @author zhangxu
 */
public enum CarError {
    MANUFACTURER_ERROR("Manufacturer is not valid, invalid value=%s"),
    LICENSEPLATE_ERROR("License is not valid, invalid value=%s"),
    SEATCOUNT_ERROR("Seat count is not valid, invalid value=%s"),
    CAR_NULL("Car should not be null"),
    CAR_SIZE_EXCEED("car.size.exceed");

    CarError(String msg) {
        this.msg = msg;
    }

    private String msg;

    public String msg() {
        return msg;
    }

}
