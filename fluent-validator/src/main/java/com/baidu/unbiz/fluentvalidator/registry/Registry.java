package com.baidu.unbiz.fluentvalidator.registry;

import java.util.List;

/**
 * Bean注册查找器
 * <p/>
 * 框架内部的<code>FluentValidator</code>使用此接口来查找验证器<code>Validator</code>实例，可以看做通用的容器查找入口。
 * 简单的通过当前的<tt>ClassLoader</tt>通过反射初始化一个<code>Validator</code>实例，也可以选择使用<tt>Spring</tt>等IoC容器，使用<code
 * >ApplicationContext</code>查找验证器<code>Validator</code>
 *
 * @author zhangxu
 */
public interface Registry {

    /**
     * 查找Bean
     *
     * @param type 类型
     *
     * @return 可找到的Bean的实例列表
     */
    <T> List<T> findByType(Class<T> type);

}
