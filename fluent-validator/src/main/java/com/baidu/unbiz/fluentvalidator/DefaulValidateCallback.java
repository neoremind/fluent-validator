package com.baidu.unbiz.fluentvalidator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangxu
 */
public class DefaulValidateCallback implements ValidateCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaulValidateCallback.class);

    @Override
    public void onSuccess(ValidatorElementList chained) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Validate on {} passed", chained);
        }
    }

    @Override
    public void onFail(ValidatorElementList chained, List<String> errorMsgs) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Validate on {} failed due to {}", chained, errorMsgs);
        }
    }

    @Override
    public void onUncaughtException(Validator validator, Throwable t, Object target) throws Throwable {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Uncaught exception occurs on {} due to {} with target={}", validator, t.getMessage(), target);
        }
        throw t;
    }

}
