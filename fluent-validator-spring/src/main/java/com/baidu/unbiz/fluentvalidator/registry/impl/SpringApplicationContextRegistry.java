package com.baidu.unbiz.fluentvalidator.registry.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.baidu.unbiz.fluentvalidator.registry.Registry;

/**
 * {@link Registry}的一种利用<tt>Spring</tt>IoC容器查找bean的实现
 *
 * @author zhangxu
 */
public class SpringApplicationContextRegistry implements Registry, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringApplicationContextRegistry.class);

    /**
     * ApplicationContext
     */
    private ApplicationContext applicationContext;

    /**
     * 默认的简单查找，当<tt>Spring</tt>IoC容器找不到时候，利用简单的方式获取
     */
    private Registry simpleRegistry = new SimpleRegistry();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> List<T> findByType(Class<T> type) {
        Map<String, T> map = findByTypeWithName(type);
        if (map == null || map.isEmpty()) {
            LOGGER.warn("Not found from Spring IoC container for " + type.getSimpleName() + ", and try to init by "
                    + "calling newInstance.");
            return simpleRegistry.findByType(type);
        }
        return new ArrayList<T>(map.values());
    }

    /**
     * 调用Spring工具类获取bean
     *
     * @param type 类类型
     *
     * @return 容器托管的bean字典
     */
    public <T> Map<String, T> findByTypeWithName(Class<T> type) {
        return BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, type);
    }

}
