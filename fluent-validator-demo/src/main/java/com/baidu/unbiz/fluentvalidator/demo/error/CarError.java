package com.baidu.unbiz.fluentvalidator.demo.error;

/**
 * @author zhangxu
 */
public enum CarError {

    MANUFACTURER_ERROR(100, "Manufacturer is not valid, invalid value=%s"),
    LICENSEPLATE_ERROR(200, "License is not valid, invalid value=%s"),
    SEATCOUNT_ERROR(300, "Seat count is not valid, invalid value=%s");

    CarError(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;

    private String msg;

    public String msg() {
        return msg;
    }

    public int code() {
        return code;
    }

}
