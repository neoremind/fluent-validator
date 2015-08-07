package com.baidu.unbiz.fluentvalidator.jsr303;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.Result;
import com.baidu.unbiz.fluentvalidator.dto.Company;
import com.baidu.unbiz.fluentvalidator.dto.CompanyBuilder;
import com.baidu.unbiz.fluentvalidator.grouping.AddCompany;
import com.baidu.unbiz.fluentvalidator.validator.CompanyCustomValidator;

/**
 * @author zhangxu
 */
public class FluentHibernateValidatorTest {

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

        Result ret = FluentHibernateValidator.checkAll()
                .on(company, new HibernateSupportedValidator<Company>().setValidator(validator))
                .on(company, new CompanyCustomValidator())
                .doValidate();
        System.out.println(ret);
        assertThat(ret.hasNoError(), is(true));
    }

    @Test
    public void testCompanyGrouping() {
        Company company = CompanyBuilder.buildSimple();

        Result ret = FluentHibernateValidator.checkAll(AddCompany.class)
                .on(company, new HibernateSupportedValidator<Company>().setValidator(validator))
                .on(company, new CompanyCustomValidator())
                .doValidate();
        System.out.println(ret);
        assertThat(ret.hasNoError(), is(false));
        assertThat(ret.getErrorNumber(), is(1));
        assertThat(ret.getErrorMsgs().get(0), is("{ceo} Company CEO is not valid"));
    }

}
