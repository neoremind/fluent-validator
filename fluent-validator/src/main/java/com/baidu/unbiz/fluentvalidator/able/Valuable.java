/**
 *
 */
package com.baidu.unbiz.fluentvalidator.able;

/**
 * 定义返回值接口，一般用于不能继承的对象，如枚举
 *
 * @author zhangxu
 */
public interface Valuable<T> {

    /**
     * 取值
     *
     * @return the value
     */
    T value();
}
