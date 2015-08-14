package com.baidu.unbiz.fluentvalidator.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

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
     * 判断是否有超类
     *
     * @param clazz 目标类
     *
     * @return 如果有返回<code>true</code>，否则返回<code>false</code>
     */
    public static boolean hasSuperClass(Class<?> clazz) {
        return (clazz != null) && !clazz.equals(Object.class);
    }

}
