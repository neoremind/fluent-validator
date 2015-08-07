package com.baidu.unbiz.fluentvalidator.jsr303;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Locale;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

import com.baidu.unbiz.fluentvalidator.DefaulValidateCallback;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.Result;
import com.baidu.unbiz.fluentvalidator.ValidatorElementList;
import com.baidu.unbiz.fluentvalidator.dto.Company;
import com.baidu.unbiz.fluentvalidator.dto.CompanyBuilder;
import com.baidu.unbiz.fluentvalidator.exception.MyException;
import com.baidu.unbiz.fluentvalidator.validator.CompanyCustomValidator;

/**
 * @author zhangxu
 */
public class HiberateSupportedValidatorTest {

    private static Validator validator;

    @BeforeClass
    public static void setUpValidator() {
        Locale.setDefault(Locale.ENGLISH);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testCompany() {
        Company company = CompanyBuilder.buildSimple();

        Result ret = FluentValidator.checkAll()
                .on(company, new HibernateSupportedValidator<Company>().setValidator(validator))
                .on(company, new CompanyCustomValidator())
                .doValidate();
        System.out.println(ret);
        assertThat(ret.hasNoError(), is(true));
    }

    @Test
    public void testCompanyNameError() {
        Company company = CompanyBuilder.buildSimple();
        company.setName("$%^$%^$%");

        Result ret = FluentValidator.checkAll()
                .on(company, new HibernateSupportedValidator<Company>().setValidator(validator))
                .doValidate();
        System.out.println(ret);
        assertThat(ret.hasNoError(), is(false));
        assertThat(ret.getErrorNumber(), is(1));
        assertThat(ret.getErrorMsgs().get(0).startsWith("{name} must match \"[0-9a-zA-Z"), is(true));
    }

//    @Test
//    public void testCompanyNameErrr() {
//        Company company = CompanyBuilder.buildSimple();
//        company.setName("$%^$%^$%");
//
//        Result ret = FluentValidator.checkAll()
//                .on(company, new HibernateSupportedValidator<Company>().setValidator(validator))
//                .doValidate();
//        System.out.println(ret);
//        assertThat(ret.hasNoError(), is(false));
//        assertThat(ret.getErrorNumber(), is(1));
//        assertThat(ret.getErrorMsgs().get(0).startsWith("name"), is(true));
//    }
//
//    @Test(expected = MyException.class)
//    public void testC() {
//        Company company = CompanyBuilder.buildSimple();
//
//        Result ret = FluentValidator.checkAll()
//                .on(company, new HibernateSupportedValidator<Company>().setValidator(validator))
//                .doValidate(new DefaulValidateCallback() {
//                    @Override
//                    public void onFail(ValidatorElementList validatorElementList, List<String> errorMsgs) {
//                        System.err.println(errorMsgs);
//                        throw new MyException("ERROR HERE!");
//                    }
//                });
//        System.out.println(ret);
//        assertThat(ret.hasNoError(), is(true));
//    }

}
