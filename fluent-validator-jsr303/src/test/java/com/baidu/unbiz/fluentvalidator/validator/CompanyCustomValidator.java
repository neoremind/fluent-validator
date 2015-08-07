package com.baidu.unbiz.fluentvalidator.validator;

import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.time.DateUtils;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.baidu.unbiz.fluentvalidator.dto.Company;
import com.baidu.unbiz.fluentvalidator.error.ErrorMsg;

/**
 * @author zhangxu
 */
public class CompanyCustomValidator extends ValidatorHandler<Company> implements Validator<Company> {

    @Override
    public boolean validate(ValidatorContext context, Company company) {
        if (company.getId() <= 0) {
            context.addErrorMsg(String.format(ErrorMsg.COMPANY_ID_INVALID.msg(), company.getId()));
            return false;
        }
        Date date = company.getEstablishTime();
        if (DateUtils.isSameDay(date, getDate(2015, 9, 1))) {
            context.addErrorMsg(String.format(ErrorMsg.COMPANY_DATE_INVALID.msg(), date));
            return false;
        }
        return true;
    }

    /**
     * 生成java.util.Date类型的对象
     *
     * @param year  int 年
     * @param month int 月
     * @param day   int 日
     *
     * @return Date java.util.Date类型的对象
     */
    public static Date getDate(int year, int month, int day) {
        GregorianCalendar d = new GregorianCalendar(year, month - 1, day);
        return d.getTime();
    }

}
