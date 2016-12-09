package com.baidu.unbiz.fluentvalidator.util;

import com.baidu.unbiz.fluentvalidator.FluentValidator;

/**
 * Fluent validator decorator, same as decorator design pattern
 *
 * @author zhangxu
 */
public interface Decorator {

    /**
     * Decorate one FluentValidator and return the new instance.
     *
     * @param fv FluentValidator
     * @return Decorated FluentValidator
     */
    FluentValidator decorate(FluentValidator fv);

}
