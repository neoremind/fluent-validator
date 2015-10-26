package com.baidu.unbiz.fluentvalidator.util;

import java.util.Locale;

/**
 * 语言地区工具类
 *
 * @author zhangxu
 */
public class LocaleUtil {

    /**
     * 解析locale字符串。
     * <p>
     * Locale字符串是符合下列格式：<code>language_country_variant</code>。
     * </p>
     *
     * @param localeString 要解析的字符串
     *
     * @return <code>Locale</code>对象，如果locale字符串为空，则返回<code>null</code>
     */
    public static Locale parseLocale(String localeString) {
        if (localeString == null || localeString.length() == 0) {
            return Locale.getDefault();
        }
        localeString = localeString.trim();

        if (localeString == null) {
            return null;
        }

        String language = "";
        String country = "";
        String variant = "";

        // language
        int start = 0;
        int index = localeString.indexOf("_");

        if (index >= 0) {
            language = localeString.substring(start, index).trim();

            // country
            start = index + 1;
            index = localeString.indexOf("_", start);

            if (index >= 0) {
                country = localeString.substring(start, index).trim();

                // variant
                variant = localeString.substring(index + 1).trim();
            } else {
                country = localeString.substring(start).trim();
            }
        } else {
            language = localeString.substring(start).trim();
        }

        return new Locale(language, country, variant);
    }

}
