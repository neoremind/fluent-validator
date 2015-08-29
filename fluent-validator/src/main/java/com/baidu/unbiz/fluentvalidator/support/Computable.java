package com.baidu.unbiz.fluentvalidator.support;

import java.util.concurrent.Callable;

/**
 * 计算接口
 */
public interface Computable<K, V> {

    /**
     * 通过关键字来计算
     *
     * @param key      查找关键字
     * @param callable # @see Callable
     *
     * @return 计算结果
     */
    V get(K key, Callable<V> callable);

}
