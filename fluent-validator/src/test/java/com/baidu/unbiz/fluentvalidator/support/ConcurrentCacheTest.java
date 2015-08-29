package com.baidu.unbiz.fluentvalidator.support;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Callable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zhangxu
 */
public class ConcurrentCacheTest {

    private Computable<String, Object> cache;

    @Before
    public void setUp() throws Exception {
        cache = ConcurrentCache.createComputable();
    }

    @After
    public void tearDown() throws Exception {
        cache = null;
    }

    @Test
    public void testGetObject() {
        final String key = "object";

        Object result1 = cache.get(key, new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                return new Object();
            }
        });

        Object result2 = cache.get(key, new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                return new Object();
            }
        });

        assertEquals(result1, result2);
    }

}