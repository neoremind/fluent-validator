package com.baidu.unbiz.fluentvalidator;

import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toComplex;
import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toSimple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import com.baidu.unbiz.fluentvalidator.util.CollectionUtil;
import com.baidu.unbiz.fluentvalidator.util.Function;
import com.baidu.unbiz.fluentvalidator.validator.StringValidator;

/**
 * @author zhangxu
 */
public class FluentValidatorStringTest {

    @Test
    public void testStringSimpleResult() {
        String str = "abc";
        Result ret = FluentValidator.checkAll()
                .on(str, new StringValidator())
                .doValidate().result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(true));
    }

    @Test
    public void testStringSimpleResultNegative() {
        String str = "123";
        Result ret = FluentValidator.checkAll()
                .on(str, new StringValidator())
                .doValidate().result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(1));
        assertThat(ret.getErrors().get(0), is("string should be abc"));
    }

    @Test
    public void testStringComplexResult() {
        String str = "abc";
        ComplexResult ret = FluentValidator.checkAll()
                .on(str, new StringValidator())
                .doValidate().result(toComplex());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(true));
    }

    @Test
    public void testStringComplexResultNegative() {
        String str = "123";
        ComplexResult ret = FluentValidator.checkAll()
                .on(str, new StringValidator())
                .doValidate().result(toComplex());
        System.out.println(ret);
        System.out.println(ret.getTimeElapsed());
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrors().get(0).getErrorCode(), is(100));
        assertThat(ret.getErrors().get(0).getErrorMsg(), is("string should be abc"));
        assertThat(ret.getErrors().get(0).getField(), is("str"));
        assertThat(ret.getErrors().get(0).getInvalidValue(), is((Object) str));
    }

    @Test
    public void testStringCustomResultCollector() {
        String str = "abc";
        MyResult ret = FluentValidator.checkAll()
                .on(str, new StringValidator())
                .doValidate().result(new ResultCollector<MyResult>() {
                    @Override
                    public MyResult toResult(ValidationResult result) {
                        MyResult ret = new MyResult();
                        if (result.isSuccess()) {
                            ret.setErrors(CollectionUtil
                                    .transform(result.getErrors(), new Function<ValidationError, MyError>() {
                                        @Override
                                        public MyError apply(ValidationError input) {
                                            MyError e = new MyError();
                                            e.errorCode = input.getErrorCode();
                                            e.errorMsg = input.getErrorMsg();
                                            e.field = input.getField();
                                            e.invalidValue = input.getInvalidValue();
                                            return e;
                                        }
                                    }));
                        }
                        return ret;
                    }
                });
        System.out.println(ret);
        assertThat(ret.hasNoError(), is(true));
    }

    @Test
    public void testStringCustomResultCollectorNegative() {
        String str = "123";
        MyResult ret = FluentValidator.checkAll()
                .on(str, new StringValidator())
                .doValidate().result(new ResultCollector<MyResult>() {
                    @Override
                    public MyResult toResult(ValidationResult result) {
                        MyResult ret = new MyResult();
                        if (!result.isSuccess()) {
                            ret.setErrors(CollectionUtil
                                    .transform(result.getErrors(), new Function<ValidationError, MyError>() {
                                        @Override
                                        public MyError apply(ValidationError input) {
                                            MyError e = new MyError();
                                            e.errorCode = input.getErrorCode();
                                            e.errorMsg = input.getErrorMsg();
                                            e.field = input.getField();
                                            e.invalidValue = input.getInvalidValue();
                                            return e;
                                        }
                                    }));
                        }
                        return ret;
                    }
                });
        System.out.println(ret);
        assertThat(ret.hasNoError(), is(false));
        assertThat(ret.getErrors().size(), is(1));
        assertThat(ret.getErrors().get(0).errorCode, is(100));
        assertThat(ret.getErrors().get(0).errorMsg, is("string should be abc"));
        assertThat(ret.getErrors().get(0).field, is("str"));
        assertThat(ret.getErrors().get(0).invalidValue, is((Object) str));
    }
}

class MyResult {

    public List<MyError> errors = CollectionUtil.createArrayList();

    public boolean hasNoError() {
        return CollectionUtil.isEmpty(errors);
    }

    public List<MyError> getErrors() {
        return errors;
    }

    public void setErrors(List<MyError> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "MyResult{" +
                "errors=" + errors +
                '}';
    }
}

class MyError {

    /**
     * 错误消息
     */
    public String errorMsg;

    /**
     * 错误字段
     * <p/>
     * 举例来说，可以是普通的字段名也可以是一个OGNL表达式，例如：
     * <ul>
     * <li>name</li>
     * <li>artist[0].gender</li>
     * </ul>
     */
    public String field;

    /**
     * 错误码
     */
    public int errorCode;

    /**
     * 错误值
     */
    public Object invalidValue;

    @Override
    public String toString() {
        return "MyError{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                ", field='" + field + '\'' +
                ", invalidValue=" + invalidValue +
                '}';
    }
}
