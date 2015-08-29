package com.baidu.unbiz.fluentvalidator.validator.element;

import java.util.List;

import com.baidu.unbiz.fluentvalidator.Const;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.able.ListAble;
import com.baidu.unbiz.fluentvalidator.util.CollectionUtil;

/**
 * 一个对象上进行多个验证器验证的元素
 *
 * @author zhangxu
 * @see ValidatorElementComposite
 */
public class MultiValidatorElement extends ValidatorElementComposite implements ListAble<ValidatorElement> {

    public MultiValidatorElement(List<ValidatorElement> validatorElements) {
        super(validatorElements);
    }

    @Override
    public String toString() {
        if (CollectionUtil.isEmpty(validatorElements)) {
            return "[]";
        }
        return validatorElements.toString();
    }

}
