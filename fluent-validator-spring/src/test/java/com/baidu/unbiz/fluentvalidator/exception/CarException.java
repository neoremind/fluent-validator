package com.baidu.unbiz.fluentvalidator.exception;

/**
 * @author zhangxu
 */
public class CarException extends RuntimeException {

    public CarException() {
    }

    public CarException(String message) {
        super(message);
    }

    public CarException(String message, Throwable cause) {
        super(message, cause);
    }

    public CarException(Throwable cause) {
        super(cause);
    }

}
