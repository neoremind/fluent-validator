package com.baidu.unbiz.fluentvalidator;

import java.util.List;

/**
 * @author zhangxu
 */
public class ValidatorChain {

    private List<Validator> validators;

    public List<Validator> getValidators() {
        return validators;
    }

    public void setValidators(List<Validator> validators) {
        this.validators = validators;
    }

}
