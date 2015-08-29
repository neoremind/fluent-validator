package com.baidu.unbiz.fluentvalidator.support;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 此类不用额外使用自旋锁或者同步器，可保证并发时线程安全，在并发情况下具有高性能
 */
public class ConcurrentCache<K, V> implements Computable<K, V> {

    protected final ConcurrentMap<K, Future<V>> concurrentMap;

    public ConcurrentCache() {
        concurrentMap = new ConcurrentHashMap<K, Future<V>>();
    }

    public static <K, V> Computable<K, V> createComputable() {
        return new ConcurrentCache<K, V>();
    }

    /**
     * 通过关键字获取数据，如果存在则直接返回，如果不存在，则通过计算<code>callable</code>来生成
     *
     * @param key      查找关键字
     * @param callable # @see Callable
     *
     * @return 计算结果
     */
    public V get(K key, Callable<V> callable) {
        Future<V> future = concurrentMap.get(key);
        if (future == null) {
            FutureTask<V> futureTask = new FutureTask<V>(callable);
            future = concurrentMap.putIfAbsent(key, futureTask);
            if (future == null) {
                future = futureTask;
                futureTask.run();
            }
        }
        try {
            // 此时阻塞
            return future.get();
        } catch (Exception e) {
            concurrentMap.remove(key);
            return null;
        }
    }
}
