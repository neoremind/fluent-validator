package com.baidu.unbiz.fluentvalidator.registry.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.unbiz.fluentvalidator.exception.ClassInstantiationException;
import com.baidu.unbiz.fluentvalidator.registry.Registry;
import com.baidu.unbiz.fluentvalidator.util.ClassUtil;
import com.baidu.unbiz.fluentvalidator.util.CollectionUtil;

/**
 * {@link Registry}的一种简单实现，直接通过反射技术初始化一个Bean返回
 *
 * @author zhangxu
 */
public class SimpleRegistry implements Registry {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleRegistry.class);

    @Override
    public <T> List<T> findByType(Class<T> type) {
        List<T> ret = CollectionUtil.createArrayList(1);
        try {
            ret.add(ClassUtil.newInstance(type));
        } catch (ClassInstantiationException e) {
            LOGGER.error("Failed to init " + type.getSimpleName());
        }
        return ret;
    }

}
