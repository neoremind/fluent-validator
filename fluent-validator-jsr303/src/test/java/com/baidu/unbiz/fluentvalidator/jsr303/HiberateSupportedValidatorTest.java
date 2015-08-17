package com.baidu.unbiz.fluentvalidator.jsr303;

import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toComplex;
import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toSimple;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Locale;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.DefaulValidateCallback;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.Result;
import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.ValidatorElementList;
import com.baidu.unbiz.fluentvalidator.dto.Company;
import com.baidu.unbiz.fluentvalidator.dto.CompanyBuilder;
import com.baidu.unbiz.fluentvalidator.dto.Department;
import com.baidu.unbiz.fluentvalidator.exception.MyException;
import com.baidu.unbiz.fluentvalidator.util.DateUtil;
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
                .on(company, new HibernateSupportedValidator<Company>().setHiberanteValidator(validator))
                .on(company, new CompanyCustomValidator())
                .doValidate().result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(true));
    }

    @Test
    public void testCompanyNameRegexError() {
        Company company = CompanyBuilder.buildSimple();
        company.setName("$%^$%^$%");

        Result ret = FluentValidator.checkAll()
                .on(company, new HibernateSupportedValidator<Company>().setHiberanteValidator(validator))
                .doValidate().result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(1));
        assertThat(ret.getErrors().get(0).startsWith("{name} must match \"[0-9a-zA-Z"), is(true));
    }

    @Test
    public void testCompanyNameEmptyError() {
        Company company = CompanyBuilder.buildSimple();
        company.setName("");

        Result ret = FluentValidator.checkAll()
                .on(company, new HibernateSupportedValidator<Company>().setHiberanteValidator(validator))
                .doValidate().result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(2));
    }

    @Test
    public void testDepartmentSizeError() {
        Company company = CompanyBuilder.buildSimple();
        for (int i = 0; i < 100; i++) {
            company.getDepartmentList().add(new Department(101, "R&D"));
        }

        Result ret = FluentValidator.checkAll()
                .on(company, new HibernateSupportedValidator<Company>().setHiberanteValidator(validator))
                .doValidate().result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(1));
        assertThat(ret.getErrors().get(0), is("{departmentList} size must be between 0 and 10"));
    }

    @Test
    public void testDepartmentNameError() {
        Company company = CompanyBuilder.buildSimple();
        company.getDepartmentList().get(0).setName("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");

        Result ret = FluentValidator.checkAll()
                .on(company, new HibernateSupportedValidator<Company>().setHiberanteValidator(validator))
                .doValidate().result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(1));
        assertThat(ret.getErrors().get(0), is("{departmentList[0].name} length must be between 0 and 30"));
    }

    @Test
    public void testCumtomValidatorError() {
        Company company = CompanyBuilder.buildSimple();
        company.setEstablishTime(DateUtil.getDate(2015, 1, 1));

        Result ret = FluentValidator.checkAll()
                .on(company, new HibernateSupportedValidator<Company>().setHiberanteValidator(validator))
                .on(company, new CompanyCustomValidator())
                .doValidate().result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(1));
        assertThat(ret.getErrors().get(0).startsWith("Company date is not valid"), is(true));
    }

    @Test
    public void testMultirError() {
        Company company = CompanyBuilder.buildSimple();
        company.setId(-1);
        company.setDepartmentList(null);

        Result ret = FluentValidator.checkAll().failOver()
                .on(company, new HibernateSupportedValidator<Company>().setHiberanteValidator(validator))
                .on(company, new CompanyCustomValidator())
                .doValidate().result(toSimple());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(2));
        assertThat(ret.getErrors().get(0), is("{departmentList} may not be null"));
        assertThat(ret.getErrors().get(1), is("Company id is not valid, invalid value=-1"));
    }

    @Test
    public void testMultirErrorComplexResult() {
        Company company = CompanyBuilder.buildSimple();
        company.setId(-1);
        company.setDepartmentList(null);

        ComplexResult ret = FluentValidator.checkAll().failOver()
                .on(company, new HibernateSupportedValidator<Company>().setHiberanteValidator(validator))
                .on(company, new CompanyCustomValidator())
                .doValidate().result(toComplex());
        System.out.println(ret);
        assertThat(ret.isSuccess(), is(false));
        assertThat(ret.getErrorNumber(), is(2));
        assertThat(ret.getErrors().get(0).getErrorMsg(), is("{departmentList} may not be null"));
        assertThat(ret.getErrors().get(0).getField(), is("departmentList"));
        assertThat(ret.getErrors().get(0).getInvalidValue(), nullValue());
        assertThat(ret.getErrors().get(1).getErrorMsg(), is("Company id is not valid, invalid value=-1"));
    }

    @Test(expected = MyException.class)
    public void testOnFailException() {
        Company company = CompanyBuilder.buildSimple();
        company.getDepartmentList().get(0).setId(null);

        Result ret = FluentValidator.checkAll()
                .on(company, new HibernateSupportedValidator<Company>().setHiberanteValidator(validator))
                .doValidate(new DefaulValidateCallback() {
                    @Override
                    public void onFail(ValidatorElementList validatorElementList, List<ValidationError> errors) {
                        System.err.println(errors);
                        throw new MyException("ERROR HERE!");
                    }
                }).result(toSimple());
        System.out.println(ret);
    }

}
