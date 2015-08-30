package com.baidu.unbiz.fluentvalidator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * 通过注解方式使用验证，利用反射缓存的属性、方法、及其对应的验证器<code>Validator</code>
 *
 * @author zhangxu
 */
public class AnnotationValidator {

    /**
     * 在POJO中定义中待验证的属性名称
     */
    private Field field;

    /**
     * 在POJO中定义，待验证值的以get或者is前缀的方法
     */
    private Method method;

    /**
     * 分组标识，在该分组内的才执行验证
     */
    private Class<?>[] groups;

    /**
     * 是否需要级联到类或者集合、数组泛型内部类做验证
     */
    private boolean isCascade;

    /**
     * 验证器
     */
    private List<Validator> validators;

    @Override
    public String toString() {
        return "AnnotationValidator{" +
                "field=" + field +
                ", method=" + method +
                ", groups=" + Arrays.toString(groups) +
                ", isCascade=" + isCascade +
                ", validators=" + validators +
                '}';
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public List<Validator> getValidators() {
        return validators;
    }

    public void setValidators(List<Validator> validators) {
        this.validators = validators;
    }

    public Class<?>[] getGroups() {
        return groups;
    }

    public void setGroups(Class<?>[] groups) {
        this.groups = groups;
    }

    public boolean isCascade() {
        return isCascade;
    }

    public void setIsCascade(boolean isCascade) {
        this.isCascade = isCascade;
    }
}
