package com.baidu.unbiz.fluentvalidator;

import java.util.Collections;

/**
 * ComplexResult with errors of an empty list not a NULL
 *
 * @author zhangxu
 */
public class ComplexResult2 extends ComplexResult {

    public ComplexResult2() {
        errors = Collections.EMPTY_LIST;
    }
}
