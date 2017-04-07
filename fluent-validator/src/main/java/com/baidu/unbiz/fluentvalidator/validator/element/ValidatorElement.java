package com.baidu.unbiz.fluentvalidator.validator.element;

import java.util.Arrays;
import java.util.List;

import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.able.ListAble;
import com.baidu.unbiz.fluentvalidator.able.ToStringable;

/**
 * 在{@link FluentValidator}内部调用使用的验证器链包装类
 *
 * @author zhangxu
 */
public class ValidatorElement implements ListAble<ValidatorElement> {

    /**
     * 验证器
     */
    private Validator validator;

    /**
     * 待验证对象
     */
    private Object target;

    /**
     * 自定义的打印信息回调
     */
    private ToStringable customizedToString;

    
    private String message;
    
    
    /**
     * create
     *
     * @param target    待验证对象
     * @param validator 验证器
     * @param message 
     */
    public ValidatorElement(Object target, Validator validator, String message) {
        this.target = target;
        this.validator = validator;
        this.setMessage(message);
    }

    
    public ValidatorElement(Object target, Validator validator) {
        this.target = target;
        this.validator = validator;
    }

    
    
    /**
     * create
     *
     * @param target             待验证对象
     * @param validator          验证器
     * @param customizedToString 自定义的打印信息回调
     */
    public ValidatorElement(Object target, Validator validator,
                            ToStringable customizedToString) {
        this.target = target;
        this.validator = validator;
        this.customizedToString = customizedToString;
    }

    public Object getTarget() {
        return target;
    }

    public Validator getValidator() {
        return validator;
    }

    @Override
    public List<ValidatorElement> getAsList() {
        return Arrays.asList(this);
    }

    @Override
    public String toString() {
        if (customizedToString != null) {
            return customizedToString.toString();
        }
        return String.format("%s@%s", target == null ? "null" : target.getClass().getSimpleName(), validator);
    }


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}

}
