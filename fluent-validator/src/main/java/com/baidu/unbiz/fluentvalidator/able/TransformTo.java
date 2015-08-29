/**
 *
 */
package com.baidu.unbiz.fluentvalidator.able;

/**
 * 转换器
 *
 * @author zhangxu
 */
public interface TransformTo<TO> {

    /**
     * 将对象转换成<tt>TO</tt>类型
     *
     * @return 转换后的结果
     */
    TO transform();

}
