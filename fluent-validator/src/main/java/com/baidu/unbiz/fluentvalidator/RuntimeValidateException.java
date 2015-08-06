package com.baidu.unbiz.fluentvalidator;

public class RuntimeValidateException extends RuntimeException {

    public RuntimeValidateException() {
    }

    public RuntimeValidateException(String message) {
        super(message);
    }

    public RuntimeValidateException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeValidateException(Throwable cause) {
        super(cause);
    }

    public RuntimeValidateException(String message,
                                    Throwable cause,
                                    boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
