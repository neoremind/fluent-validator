package com.baidu.unbiz.fluentvalidator.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author zhangxu
 */
public class ClassUtilTest {

    @Test
    public void getWrapperTypeIfPrimitive() {
        assertEquals(Integer.class, ClassUtil.getWrapperTypeIfPrimitive(int.class));
        assertEquals(Long.class, ClassUtil.getWrapperTypeIfPrimitive(long.class));
        assertEquals(Short.class, ClassUtil.getWrapperTypeIfPrimitive(short.class));
        assertEquals(Double.class, ClassUtil.getWrapperTypeIfPrimitive(double.class));
        assertEquals(Float.class, ClassUtil.getWrapperTypeIfPrimitive(float.class));
        assertEquals(Character.class, ClassUtil.getWrapperTypeIfPrimitive(char.class));
        assertEquals(Byte.class, ClassUtil.getWrapperTypeIfPrimitive(byte.class));
        assertEquals(Boolean.class, ClassUtil.getWrapperTypeIfPrimitive(boolean.class));
        assertEquals(Void.class, ClassUtil.getWrapperTypeIfPrimitive(void.class));

        assertEquals(int[][].class, ClassUtil.getWrapperTypeIfPrimitive(int[][].class));
        assertEquals(long[][].class, ClassUtil.getWrapperTypeIfPrimitive(long[][].class));
        assertEquals(short[][].class, ClassUtil.getWrapperTypeIfPrimitive(short[][].class));
        assertEquals(double[][].class, ClassUtil.getWrapperTypeIfPrimitive(double[][].class));
        assertEquals(float[][].class, ClassUtil.getWrapperTypeIfPrimitive(float[][].class));
        assertEquals(char[][].class, ClassUtil.getWrapperTypeIfPrimitive(char[][].class));
        assertEquals(byte[][].class, ClassUtil.getWrapperTypeIfPrimitive(byte[][].class));
        assertEquals(boolean[][].class, ClassUtil.getWrapperTypeIfPrimitive(boolean[][].class));

        assertEquals(String.class, ClassUtil.getWrapperTypeIfPrimitive(String.class));

        assertEquals(String[][].class, ClassUtil.getWrapperTypeIfPrimitive(String[][].class));
    }

}
