package com.baidu.unbiz.fluentvalidator.util;

/**
 * Represents a supplier of results.
 *
 * @author zhangxu
 */
public interface Supplier<T> {

    /**
     * Gets a result.
     */
    T get();
}
