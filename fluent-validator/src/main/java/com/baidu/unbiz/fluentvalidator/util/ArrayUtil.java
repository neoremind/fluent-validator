package com.baidu.unbiz.fluentvalidator.util;

/**
 * 数组工具类
 *
 * @author zhangxu
 */
public class ArrayUtil {

    /**
     * 检查数组是否为<code>null</code>或空数组<code>[]</code>。
     * <p/>
     * <p/>
     * <pre>
     * ArrayUtil.isEmpty(null)              = true
     * ArrayUtil.isEmpty(new int[0])        = true
     * ArrayUtil.isEmpty(new int[10])       = false
     * </pre>
     *
     * @param array 要检查的数组
     *
     * @return 如果不为空, 则返回<code>true</code>
     */
    public static <T> boolean isEmpty(T[] arrary) {
        return arrary == null || arrary.length == 0;
    }

    /**
     * 验证数组是否有交集
     * <p/>
     * <pre>
     * Class<?>[] from = new Class<?>[] {};
     * Class<?>[] target = new Class<?>[] {};
     * assertThat(ArrayUtil.hasIntersection(from, target), Matchers.is(true));
     *
     * from = new Class<?>[] {String.class};
     * target = new Class<?>[] {};
     * assertThat(ArrayUtil.hasIntersection(from, target), Matchers.is(true));
     *
     * from = new Class<?>[] {};
     * target = new Class<?>[] {String.class};
     * assertThat(ArrayUtil.hasIntersection(from, target), Matchers.is(false));
     *
     * from = new Class<?>[] {String.class};
     * target = new Class<?>[] {String.class};
     * assertThat(ArrayUtil.hasIntersection(from, target), Matchers.is(true));
     *
     * from = new Class<?>[] {String.class, Object.class};
     * target = new Class<?>[] {String.class};
     * assertThat(ArrayUtil.hasIntersection(from, target), Matchers.is(true));
     *
     * from = new Class<?>[] {String.class};
     * target = new Class<?>[] {String.class, Object.class};
     * assertThat(ArrayUtil.hasIntersection(from, target), Matchers.is(true));
     *
     * from = new Class<?>[] {Integer.class};
     * target = new Class<?>[] {Object.class};
     * assertThat(ArrayUtil.hasIntersection(from, target), Matchers.is(false));
     *
     * </pre>
     *
     * @param from   基础数组
     * @param target 目标数组，看是否存在于基础数组中
     *
     * @return 如果有交集, 则返回<code>true</code>
     */
    public static <T> boolean hasIntersection(T[] from, T[] target) {
        if (isEmpty(target)) {
            return true;
        }

        if (isEmpty(from)) {
            return false;
        }

        for (int i = 0; i < from.length; i++) {
            for (int j = 0; j < target.length; j++) {
                if (from[i] == target[j]) {
                    return true;
                }
            }
        }
        return false;
    }

}
