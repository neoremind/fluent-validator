package com.baidu.unbiz.fluentvalidator.util;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Test;

import com.baidu.unbiz.fluentvalidator.sample.AnnotationClass;

/**
 * @author zhangxu
 */
public class ReflectionUtilTest {

    @Test
    public void testGetAnnotationFields() {
        assertNull(ReflectionUtil.getAnnotationFields((Class<?>) null, (Class<? extends Annotation>) null));

        assertNull(ReflectionUtil.getAnnotationFields((Class<?>) null, AnnotationClass.TestAnnotation.class));
        assertNull(ReflectionUtil.getAnnotationFields(AnnotationClass.class, (Class<? extends Annotation>) null));

        Field[] fields =
                ReflectionUtil.getAnnotationFields(AnnotationClass.class, AnnotationClass.TestAnnotation.class);

        assertThat(fields.length, is(2));

        fields = ReflectionUtil.getAnnotationFields(AnnotationClass.class, Test.class);

        assertThat(fields.length, is(0));

    }

}
