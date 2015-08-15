package com.baidu.unbiz.fluentvalidator.exception;

/**
 * 代表实例化类时失败的异常
 *
 * @author zhangxu
 */
public class ClassInstantiationException extends RuntimeException {

    public ClassInstantiationException() {
    }

    public ClassInstantiationException(String message) {
        super(message);
    }

    public ClassInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassInstantiationException(Throwable cause) {
        super(cause);
    }
}
