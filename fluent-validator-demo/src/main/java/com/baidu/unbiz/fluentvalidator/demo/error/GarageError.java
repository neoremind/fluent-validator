package com.baidu.unbiz.fluentvalidator.demo.error;

/**
 * @author zhangxu
 */
public enum GarageError {

    CAR_NUM_EXCEED_LIMIT(900, "car.size.exceed");

    GarageError(int code, String msg) {
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
