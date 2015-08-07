package com.baidu.unbiz.fluentvalidator.validator;

import java.util.Date;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.baidu.unbiz.fluentvalidator.dto.Company;
import com.baidu.unbiz.fluentvalidator.error.ErrorMsg;
import com.baidu.unbiz.fluentvalidator.util.DateUtil;

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

        // company established time must before 2010-1-1
        if (date.after(DateUtil.getDate(2010, 1, 1))) {
            context.addErrorMsg(String.format(ErrorMsg.COMPANY_DATE_INVALID.msg(), date));
            return false;
        }
        return true;
    }

}
