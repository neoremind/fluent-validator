package com.baidu.unbiz.fluentvalidator.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.baidu.unbiz.fluentvalidator.exception.RuntimeValidateException;

/**
 * 反射工具类
 *
 * @author zhangxu
 */
public class ReflectionUtil {

    /**
     * 要求参数不为<code>null</code>
     * <p/>
     * 获取类及父类的所有<code>Field</code>，不包括<code>Object</code>的 <code>Field</code>
     *
     * @param clazz 要获取的类
     *
     * @return <code>Field</code>列表
     */
    static List<Field> getAllFieldsOfClass0(Class<?> clazz) {
        List<Field> fields = CollectionUtil.createArrayList();
        for (Class<?> itr = clazz; hasSuperClass(itr); ) {
            fields.addAll(Arrays.asList(itr.getDeclaredFields()));
            itr = itr.getSuperclass();
        }

        return fields;
    }

    /**
     * 获取所有包含指定<code>Annotation</code>的<code>Field</code>数组
     *
     * @param clazz           查找类
     * @param annotationClass 注解类名
     *
     * @return <code>Field</code>数组
     */
    public static Field[] getAnnotationFields(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        if (clazz == null || annotationClass == null) {
            return null;
        }

        List<Field> fields = getAllFieldsOfClass0(clazz);
        if (CollectionUtil.isEmpty(fields)) {
            return null;
        }

        List<Field> list = CollectionUtil.createArrayList();
        for (Field field : fields) {
            if (null != field.getAnnotation(annotationClass)) {
                list.add(field);
                field.setAccessible(true);
            }
        }

        return list.toArray(new Field[0]);
    }

    /**
     * 获取<code>Field</code>上的注解
     *
     * @param field          属性
     * @param annotationType 注解类型
     *
     * @return 注解对象
     */
    public static <A extends Annotation> A getAnnotation(Field field, Class<A> annotationType) {
        if (field == null || annotationType == null) {
            return null;
        }

        return field.getAnnotation(annotationType);
    }

    /**
     * 判断是否有超类
     *
     * @param clazz 目标类
     *
     * @return 如果有返回<code>true</code>，否则返回<code>false</code>
     */
    public static boolean hasSuperClass(Class<?> clazz) {
        return (clazz != null) && !clazz.equals(Object.class);
    }

    /**
     * getter方法的前缀
     */
    private static final String GETTER_METHOD_PREFIX = "get";

    /**
     * boolean类型属性getter方法的前缀
     */
    private static final String GETTER_METHOD_PREFIX_FOR_BOOLEAN = "is";

    /**
     * 获取getter方法
     *
     * @param clazz 类
     * @param field 属性
     *
     * @return getter方法
     */
    public static Method getGetterMethod(Class<?> clazz, Field field) {
        return getMethod(clazz, getGetterMethodName(field));
    }

    /**
     * 获取getter方法名
     *
     * @param field 属性
     *
     * @return getter方法名
     */
    public static String getGetterMethodName(Field field) {
        String prefix;
        String fieldName = field.getName();
        if (field.getType() != boolean.class || field.getType() != Boolean.class) {
            prefix = GETTER_METHOD_PREFIX;
        } else {
            prefix = GETTER_METHOD_PREFIX_FOR_BOOLEAN;
        }
        return prefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    /**
     * 获取方法
     *
     * @param clazz          类
     * @param methodName     方法名
     * @param parameterTypes 方法参数
     *
     * @return 方法
     */
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        if (clazz == null || methodName == null || methodName.length() == 0) {
            return null;
        }

        for (Class<?> itr = clazz; hasSuperClass(itr); ) {
            Method[] methods = itr.getDeclaredMethods();

            for (Method method : methods) {
                if (method.getName().equals(methodName) && Arrays.equals(method.getParameterTypes(), parameterTypes)) {
                    return method;
                }
            }

            itr = itr.getSuperclass();
        }
        return null;
    }

    /**
     * 方法调用，如果<code>clazz</code>为<code>null</code>，返回<code>null</code>；
     * <p/>
     * 如果<code>method</code>为<code>null</code>，返回<code>null</code>
     * <p/>
     * 如果<code>target</code>为<code>null</code>，则为静态方法
     *
     * @param method 调用的方法
     * @param target 目标对象
     * @param args   方法的参数值
     *
     * @return 调用结果
     */
    public static <T> T invokeMethod(Method method, Object target, Object... args) {
        if (method == null) {
            return null;
        }

        method.setAccessible(true);
        try {
            @SuppressWarnings("unchecked")
            T result = (T) method.invoke(target, args);
            return result;
        } catch (Exception e) {
            throw new RuntimeValidateException(e);
        }
    }

}
