package com.baidu.unbiz.fluentvalidator;

/**
 * 内部使用的验证结果包含的错误
 *
 * @author zhangxu
 */
public class ValidationError {

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 错误字段
     * <p/>
     * 举例来说，可以是普通的字段名也可以是一个OGNL表达式，例如：
     * <ul>
     * <li>name</li>
     * <li>artist[0].gender</li>
     * </ul>
     */
    private String field;

    /**
     * 错误码
     */
    private int errorCode;

    /**
     * 错误值
     */
    private Object invalidValue;

    @Override
    public String toString() {
        return "ValidationError{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                ", field='" + field + '\'' +
                ", invalidValue=" + invalidValue +
                '}';
    }

    /**
     * 静态构造方法
     *
     * @param errorMsg 错误信息，其他信息可以省略，但是一个错误认为错误消息决不可少
     *
     * @return ValidationError
     */
    public static ValidationError create(String errorMsg) {
        return new ValidationError().setErrorMsg(errorMsg);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public ValidationError setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public ValidationError setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public String getField() {
        return field;
    }

    public ValidationError setField(String field) {
        this.field = field;
        return this;
    }

    public Object getInvalidValue() {
        return invalidValue;
    }

    public ValidationError setInvalidValue(Object invalidValue) {
        this.invalidValue = invalidValue;
        return this;
    }

}
