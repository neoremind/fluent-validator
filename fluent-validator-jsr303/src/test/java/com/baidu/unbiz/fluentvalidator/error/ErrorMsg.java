package com.baidu.unbiz.fluentvalidator.error;

/**
 * @author zhangxu
 */
public enum ErrorMsg {

    COMPANY_ID_INVALID("Company id is not valid, invalid value=%s"),
    COMPANY_DATE_INVALID("Company date is not valid, invalid value=%s");

    ErrorMsg(String msg) {
        this.msg = msg;
    }

    private String msg;

    public String msg() {
        return msg;
    }

}
