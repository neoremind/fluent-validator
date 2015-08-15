package com.baidu.unbiz.fluentvalidator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
     * 验证器
     */
    private List<Validator> validators;

    @Override
    public String toString() {
        return "AnnotationValidator{" +
                "field=" + field.getName() +
                ", method=" + method.getName() +
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
}
