package com.baidu.unbiz.fluentvalidator.validator.element;

import java.util.Arrays;
import java.util.List;

import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
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

    /**
     * 默认的错误信息，用于直接在fluent的链条上直接指定错误，如下所示
     * <pre>
     *     fv.on(car.getLicensePlate(), new CarLicensePlateValidator(),"Car License Plate Validator is Good")
     * </pre>
     * 在{@link Validator#validate(ValidatorContext, Object)}可以直接通过context获取message，
     * 例如{@link ValidatorContext#getDefaultMessage()}。
     */
    private String message;

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

    /**
     * create
     *
     * @param target    待验证对象
     * @param validator 验证器
     * @param message   默认的关联此validator的信息，一般是默认的错误信息
     */
    public ValidatorElement(Object target, Validator validator, String message) {
        this.target = target;
        this.validator = validator;
        this.message = message;
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

    public String getMessage() {
        return message;
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

}
