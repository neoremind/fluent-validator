package com.baidu.unbiz.fluentvalidator.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author zhangxu
 */
public class ClassUtilTest {

    @Test
    public void getWrapperTypeIfPrimitive_int() {
        assertEquals(Integer.class, ClassUtil.getWrapperTypeIfPrimitive(int.class));
    }

    @Test
    public void getWrapperTypeIfPrimitive_long() {
        assertEquals(Long.class, ClassUtil.getWrapperTypeIfPrimitive(long.class));
    }

    @Test
    public void getWrapperTypeIfPrimitive_short() {
        assertEquals(Short.class, ClassUtil.getWrapperTypeIfPrimitive(short.class));
    }

    @Test
    public void getWrapperTypeIfPrimitive_double() {
        assertEquals(Double.class, ClassUtil.getWrapperTypeIfPrimitive(double.class));
    }

    @Test
    public void getWrapperTypeIfPrimitive_float() {
        assertEquals(Float.class, ClassUtil.getWrapperTypeIfPrimitive(float.class));
    }

    @Test
    public void getWrapperTypeIfPrimitive_char() {
        assertEquals(Character.class, ClassUtil.getWrapperTypeIfPrimitive(char.class));
    }

    @Test
    public void getWrapperTypeIfPrimitive_byte() {
        assertEquals(Byte.class, ClassUtil.getWrapperTypeIfPrimitive(byte.class));
    }

    @Test
    public void getWrapperTypeIfPrimitive_boolean() {
        assertEquals(Boolean.class, ClassUtil.getWrapperTypeIfPrimitive(boolean.class));
    }

    @Test
    public void getWrapperTypeIfPrimitive_void() {
        assertEquals(Void.class, ClassUtil.getWrapperTypeIfPrimitive(void.class));
    }

    @Test
    public void getWrapperTypeIfPrimitive_String() {
        assertEquals(String.class, ClassUtil.getWrapperTypeIfPrimitive(String.class));
    }

    @Test
    public void getWrapperTypeIfPrimitive_int2dArray() {
        assertEquals(int[][].class, ClassUtil.getWrapperTypeIfPrimitive(int[][].class));
    }

    @Test
    public void getWrapperTypeIfPrimitive_long2dArray() {
        assertEquals(long[][].class, ClassUtil.getWrapperTypeIfPrimitive(long[][].class));
    }

    @Test
    public void getWrapperTypeIfPrimitive_short2dArray() {
        assertEquals(short[][].class, ClassUtil.getWrapperTypeIfPrimitive(short[][].class));
    }

    @Test
    public void getWrapperTypeIfPrimitive_double2dArray() {
        assertEquals(double[][].class, ClassUtil.getWrapperTypeIfPrimitive(double[][].class));
    }

    @Test
    public void getWrapperTypeIfPrimitive_float2dArray() {
        assertEquals(float[][].class, ClassUtil.getWrapperTypeIfPrimitive(float[][].class));
    }

    @Test
    public void getWrapperTypeIfPrimitive_char2dArray() {
        assertEquals(char[][].class, ClassUtil.getWrapperTypeIfPrimitive(char[][].class));
    }

    @Test
    public void getWrapperTypeIfPrimitive_byte2dArray() {
        assertEquals(byte[][].class, ClassUtil.getWrapperTypeIfPrimitive(byte[][].class));
    }

    @Test
    public void getWrapperTypeIfPrimitive_boolean2dArray() {
        assertEquals(boolean[][].class, ClassUtil.getWrapperTypeIfPrimitive(boolean[][].class));
    }

    @Test
    public void getWrapperTypeIfPrimitive_String2dArray() {
        assertEquals(String[][].class, ClassUtil.getWrapperTypeIfPrimitive(String[][].class));
    }
}
