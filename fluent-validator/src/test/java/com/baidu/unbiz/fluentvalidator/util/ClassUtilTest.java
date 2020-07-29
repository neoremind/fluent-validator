package com.baidu.unbiz.fluentvalidator.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author zhangxu
 */
public class ClassUtilTest {

    @Test
    public void getWrapperTypeIfPrimitiveInt() {
        assertEquals(Integer.class, ClassUtil.getWrapperTypeIfPrimitive(int.class));
    }

    @Test
    public void getWrapperTypeIfPrimitiveLong() {
        assertEquals(Long.class, ClassUtil.getWrapperTypeIfPrimitive(long.class));
    }

    @Test
    public void getWrapperTypeIfPrimitiveShort() {
        assertEquals(Short.class, ClassUtil.getWrapperTypeIfPrimitive(short.class));
    }

    @Test
    public void getWrapperTypeIfPrimitiveDouble() {
        assertEquals(Double.class, ClassUtil.getWrapperTypeIfPrimitive(double.class));
    }

    @Test
    public void getWrapperTypeIfPrimitiveFloat() {
        assertEquals(Float.class, ClassUtil.getWrapperTypeIfPrimitive(float.class));
    }

    @Test
    public void getWrapperTypeIfPrimitiveChar() {
        assertEquals(Character.class, ClassUtil.getWrapperTypeIfPrimitive(char.class));
    }

    @Test
    public void getWrapperTypeIfPrimitiveByte() {
        assertEquals(Byte.class, ClassUtil.getWrapperTypeIfPrimitive(byte.class));
    }

    @Test
    public void getWrapperTypeIfPrimitiveBoolean() {
        assertEquals(Boolean.class, ClassUtil.getWrapperTypeIfPrimitive(boolean.class));
    }

    @Test
    public void getWrapperTypeIfPrimitiveVoid() {
        assertEquals(Void.class, ClassUtil.getWrapperTypeIfPrimitive(void.class));
    }

    @Test
    public void getWrapperTypeIfPrimitiveString() {
        assertEquals(String.class, ClassUtil.getWrapperTypeIfPrimitive(String.class));
    }

    @Test
    public void getWrapperTypeIfPrimitiveInt2dArray() {
        assertEquals(int[][].class, ClassUtil.getWrapperTypeIfPrimitive(int[][].class));
    }

    @Test
    public void getWrapperTypeIfPrimitiveLong2dArray() {
        assertEquals(long[][].class, ClassUtil.getWrapperTypeIfPrimitive(long[][].class));
    }

    @Test
    public void getWrapperTypeIfPrimitiveShort2dArray() {
        assertEquals(short[][].class, ClassUtil.getWrapperTypeIfPrimitive(short[][].class));
    }

    @Test
    public void getWrapperTypeIfPrimitiveDouble2dArray() {
        assertEquals(double[][].class, ClassUtil.getWrapperTypeIfPrimitive(double[][].class));
    }

    @Test
    public void getWrapperTypeIfPrimitiveFloat2dArray() {
        assertEquals(float[][].class, ClassUtil.getWrapperTypeIfPrimitive(float[][].class));
    }

    @Test
    public void getWrapperTypeIfPrimitiveChar2dArray() {
        assertEquals(char[][].class, ClassUtil.getWrapperTypeIfPrimitive(char[][].class));
    }

    @Test
    public void getWrapperTypeIfPrimitiveByte2dArray() {
        assertEquals(byte[][].class, ClassUtil.getWrapperTypeIfPrimitive(byte[][].class));
    }

    @Test
    public void getWrapperTypeIfPrimitiveBoolean2dArray() {
        assertEquals(boolean[][].class, ClassUtil.getWrapperTypeIfPrimitive(boolean[][].class));
    }

    @Test
    public void getWrapperTypeIfPrimitiveString2dArray() {
        assertEquals(String[][].class, ClassUtil.getWrapperTypeIfPrimitive(String[][].class));
    }
}
