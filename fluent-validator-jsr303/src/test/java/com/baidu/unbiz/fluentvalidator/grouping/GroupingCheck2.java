package com.baidu.unbiz.fluentvalidator.grouping;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

/**
 * If at least one constraint fails in a sequenced group none of the constraints of the following groups in the
 * sequence get validated.
 *
 * @author zhangxu
 */
@GroupSequence({Default.class, AddCompany.class})
public interface GroupingCheck2 {
}
