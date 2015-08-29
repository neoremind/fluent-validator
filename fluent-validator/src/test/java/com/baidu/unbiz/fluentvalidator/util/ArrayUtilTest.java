package com.baidu.unbiz.fluentvalidator.util;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * @author zhangxu
 */
public class ArrayUtilTest {

    @Test
    public void testIsEmpty() {
        assertThat(ArrayUtil.isEmpty(null), Matchers.is(true));
        assertThat(ArrayUtil.isEmpty(new Integer[0]), Matchers.is(true));
        assertThat(ArrayUtil.isEmpty(new Integer[10]), Matchers.is(false));
    }

    @Test
    public void testHasIntersection() {
        Class<?>[] from = new Class<?>[] {};
        Class<?>[] target = new Class<?>[] {};
        assertThat(ArrayUtil.hasIntersection(from, target), Matchers.is(true));

        from = new Class<?>[] {String.class};
        target = new Class<?>[] {};
        assertThat(ArrayUtil.hasIntersection(from, target), Matchers.is(true));

        from = new Class<?>[] {};
        target = new Class<?>[] {String.class};
        assertThat(ArrayUtil.hasIntersection(from, target), Matchers.is(false));

        from = new Class<?>[] {String.class};
        target = new Class<?>[] {String.class};
        assertThat(ArrayUtil.hasIntersection(from, target), Matchers.is(true));

        from = new Class<?>[] {String.class, Object.class};
        target = new Class<?>[] {String.class};
        assertThat(ArrayUtil.hasIntersection(from, target), Matchers.is(true));

        from = new Class<?>[] {String.class};
        target = new Class<?>[] {String.class, Object.class};
        assertThat(ArrayUtil.hasIntersection(from, target), Matchers.is(true));

        from = new Class<?>[] {Integer.class};
        target = new Class<?>[] {Object.class};
        assertThat(ArrayUtil.hasIntersection(from, target), Matchers.is(false));
    }

}
