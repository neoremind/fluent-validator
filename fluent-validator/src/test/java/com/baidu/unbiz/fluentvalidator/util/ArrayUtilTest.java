package com.baidu.unbiz.fluentvalidator.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
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

    @Test
    public void primitiveToWrapper() {
        assertNull(ArrayUtil.toWrapperIfPrimitive(null));

        String[] strArray = ArrayUtil.toWrapperIfPrimitive(new String[0]);
        assertArrayEquals(new String[0], strArray);

        Boolean[] booleanArray = ArrayUtil.toWrapperIfPrimitive(new boolean[] {true, false, true});
        assertArrayEquals(new Boolean[] {true, false, true}, booleanArray);

        Character[] charArray = ArrayUtil.toWrapperIfPrimitive(new char[] {'1', '2', '3'});
        assertArrayEquals(new Character[] {'1', '2', '3'}, charArray);

        Byte[] byteArray = ArrayUtil.toWrapperIfPrimitive(new byte[] {1, 2, 3});
        assertArrayEquals(new Byte[] {1, 2, 3}, byteArray);

        Short[] shortArray = ArrayUtil.toWrapperIfPrimitive(new short[] {1, 2, 3});
        assertArrayEquals(new Short[] {1, 2, 3}, shortArray);

        Integer[] intArray = ArrayUtil.toWrapperIfPrimitive(new int[] {1, 2, 3});
        assertArrayEquals(new Integer[] {1, 2, 3}, intArray);

        Long[] longArray = ArrayUtil.toWrapperIfPrimitive(new long[] {1L, 2L, 3L});
        assertArrayEquals(new Long[] {1L, 2L, 3L}, longArray);

        Float[] floatArray = ArrayUtil.toWrapperIfPrimitive(new float[] {1f, 2f, 3f});
        assertArrayEquals(new Float[] {1f, 2f, 3f}, floatArray);

        Double[] doubleArray = ArrayUtil.toWrapperIfPrimitive(new double[] {1d, 2d, 3d});
        assertArrayEquals(new Double[] {1d, 2d, 3d}, doubleArray);

    }

}
