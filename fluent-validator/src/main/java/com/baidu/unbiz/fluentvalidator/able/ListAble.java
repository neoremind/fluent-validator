package com.baidu.unbiz.fluentvalidator.able;

import java.util.List;

/**
 * 可供转化为列表的接口
 *
 * @author zhangxu
 */
public interface ListAble<T> {

    /**
     * 按照列表形态获取
     *
     * @return 列表
     */
    List<T> getAsList();

    /**
     * 重写{@link #toString()}方法
     *
     * @return 字面输出含义
     */
    String toString();

}
