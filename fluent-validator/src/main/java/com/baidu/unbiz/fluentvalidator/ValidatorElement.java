package com.baidu.unbiz.fluentvalidator;

/**
 * @author zhangxu
 */
class ValidatorElement {

    private Validator validator;

    private Object target;

    public ValidatorElement(Object target, Validator validator, Closure... closures) {
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
