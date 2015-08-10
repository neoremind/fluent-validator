package com.baidu.unbiz.fluentvalidator.jsr303;

/**
 * 线程执行的上下文内容
 *
 * @author zhangxu
 */
public class GroupingHolder {

    /**
     * 线程上下文变量的持有者
     */
    private static final ThreadLocal<Class[]> CTX_HOLDER = new ThreadLocal<Class[]>();

    /**
     * 清空线程上下文
     */
    public static final void clean() {
        CTX_HOLDER.set(null);
    }

    /**
     * 初始化线程上下文
     */
    public static final void setGrouping(Class[] clazz) {
        CTX_HOLDER.set(clazz);
    }

    /**
     * 初始化线程上下文
     */
    public static final Class[] getGrouping() {
        return CTX_HOLDER.get();
    }

}
