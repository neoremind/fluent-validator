package com.baidu.unbiz.fluentvalidator.util;

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

}
