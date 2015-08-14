package com.baidu.unbiz.fluentvalidator.jsr303;

import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.Result;
import com.baidu.unbiz.fluentvalidator.ValidateCallback;

/**
 * 为Hibernate Validator定制的FluentValidator
 *
 * @author zhangxu
 */
public class FluentHibernateValidator extends FluentValidator {

    /**
     * Groupings
     */
    private Class[] groupingConstraints;

    /**
     * 创建<tt>FluentHibernateValidator</tt>
     *
     * @return FluentHibernateValidator
     */
    public static FluentHibernateValidator checkAll(Class... groupingConstraints) {
        return new FluentHibernateValidator().setGroupingConstraints(groupingConstraints);
    }

    /**
     * 按照默认验证回调条件，开始使用验证
     *
     * @return FluentValidator
     */
    public FluentValidator doValidate() {
        if (groupingConstraints == null) {
            return doValidate(defaultCb);
        }

        try {
            GroupingHolder.setGrouping(groupingConstraints);
            return super.doValidate(defaultCb);
        } finally {
            GroupingHolder.clean();
        }
    }

    /**
     * 按照指定验证回调条件，开始使用验证
     *
     * @param cb 验证回调
     *
     * @return FluentValidator
     *
     * @see ValidateCallback
     */
    public FluentValidator doValidate(ValidateCallback cb) {
        if (groupingConstraints == null) {
            return doValidate(cb);
        }

        try {
            GroupingHolder.setGrouping(groupingConstraints);
            return super.doValidate(cb);
        } finally {
            GroupingHolder.clean();
        }
    }

    public FluentHibernateValidator setGroupingConstraints(Class[] groupingConstraints) {
        this.groupingConstraints = groupingConstraints;
        return this;
    }
}
