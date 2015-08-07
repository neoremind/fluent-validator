package com.baidu.unbiz.fluentvalidator.util;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author zhangxu
 */
public class DateUtil {

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
