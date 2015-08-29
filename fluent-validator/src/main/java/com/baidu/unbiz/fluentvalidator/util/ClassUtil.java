package com.baidu.unbiz.fluentvalidator.util;

import java.util.HashMap;
import java.util.Map;

import com.baidu.unbiz.fluentvalidator.exception.ClassInstantiationException;

/**
 * 类工具
 *
 * @author zhangxu
 */
public class ClassUtil {

    /**
     * 创建指定类的实例。
     *
     * @param clazz 要创建实例的类
     *
     * @return 指定类的实例
     *
     * @throws ClassInstantiationException 如果实例化失败
     */
    public static <T> T newInstance(Class<T> clazz) throws ClassInstantiationException {
        if (clazz == null) {
            return null;
        }

        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new ClassInstantiationException("Failed to instantiate class: " + clazz.getName(), e);
        }
    }

    public static <T> Class<T> getWrapperTypeIfPrimitive(Class<T> type) {
        if (type == null) {
            return null;
        }

        if (type.isPrimitive()) {
            return ((PrimitiveInfo<T>) PRIMITIVES.get(type.getName())).wrapperType;
        }

        return type;
    }

    private static final Map<String, PrimitiveInfo<?>> PRIMITIVES = new HashMap<String, PrimitiveInfo<?>>(13);

    static {
        addPrimitive(boolean.class, "Z", Boolean.class, "booleanValue", false, Boolean.FALSE, Boolean.TRUE);
        addPrimitive(short.class, "S", Short.class, "shortValue", (short) 0, Short.MAX_VALUE, Short.MIN_VALUE);
        addPrimitive(int.class, "I", Integer.class, "intValue", 0, Integer.MAX_VALUE, Integer.MIN_VALUE);
        addPrimitive(long.class, "J", Long.class, "longValue", 0L, Long.MAX_VALUE, Long.MIN_VALUE);
        addPrimitive(float.class, "F", Float.class, "floatValue", 0F, Float.MAX_VALUE, Float.MIN_VALUE);
        addPrimitive(double.class, "D", Double.class, "doubleValue", 0D, Double.MAX_VALUE, Double.MIN_VALUE);
        addPrimitive(char.class, "C", Character.class, "charValue", '\0', Character.MAX_VALUE, Character.MIN_VALUE);
        addPrimitive(byte.class, "B", Byte.class, "byteValue", (byte) 0, Byte.MAX_VALUE, Byte.MIN_VALUE);
        addPrimitive(void.class, "V", Void.class, null, null, null, null);
    }

    private static <T> void addPrimitive(Class<T> type, String typeCode, Class<T> wrapperType, String unwrapMethod,
                                         T defaultValue, T maxValue, T minValue) {
        PrimitiveInfo<T> info =
                new PrimitiveInfo<T>(type, typeCode, wrapperType, unwrapMethod, defaultValue, maxValue, minValue);

        PRIMITIVES.put(type.getName(), info);
        PRIMITIVES.put(wrapperType.getName(), info);
    }

    /**
     * 代表一个primitive类型的信息。
     */
    private static class PrimitiveInfo<T> {
        final Class<T> type;
        @SuppressWarnings("unused")
        final String typeCode;
        final Class<T> wrapperType;
        @SuppressWarnings("unused")
        final String unwrapMethod;
        final T defaultValue;
        final T maxValue;
        final T minValue;

        public PrimitiveInfo(Class<T> type, String typeCode, Class<T> wrapperType, String unwrapMethod, T defaultValue,
                             T maxValue, T minValue) {
            this.type = type;
            this.typeCode = typeCode;
            this.wrapperType = wrapperType;
            this.unwrapMethod = unwrapMethod;
            this.defaultValue = defaultValue;
            this.maxValue = maxValue;
            this.minValue = minValue;
        }
    }

}
