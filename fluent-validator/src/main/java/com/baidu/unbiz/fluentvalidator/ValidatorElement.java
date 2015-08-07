package com.baidu.unbiz.fluentvalidator;

/**
 * 在{@link FluentValidator}内部调用使用的验证器链包装类
 *
 * @author zhangxu
 */
class ValidatorElement {

    /**
     * 验证器
     */
    private Validator validator;

    /**
     * 待验证对象
     */
    private Object target;

    /**
     * create
     *
     * @param target    待验证对象
     * @param validator 验证器
     */
    public ValidatorElement(Object target, Validator validator) {
        this.target = target;
        this.validator = validator;
    }

    public Object getTarget() {
        return target;
    }

    public Validator getValidator() {
        return validator;
    }

}
