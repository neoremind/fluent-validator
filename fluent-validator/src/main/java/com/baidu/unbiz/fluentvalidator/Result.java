package com.baidu.unbiz.fluentvalidator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangxu
 */
public class Result {

    private List<String> errorMsgs;

    @Override
    public String toString() {
        return String.format("Result{hasError=%s, errorMsgs=%s}", hasError(), errorMsgs);
    }

    public int getErrorNumber() {
        return errorMsgs == null ? 0 : errorMsgs.size();
    }

    public boolean hasNoError() {
        return errorMsgs == null || errorMsgs.size() == 0;
    }

    public boolean hasError() {
        return !hasNoError();
    }

    public List<String> getErrorMsgs() {
        return errorMsgs;
    }

    public void addErrorMsg(String msg) {
        if (errorMsgs == null) {
            errorMsgs = new ArrayList<String>(4);
        }
        errorMsgs.add(msg);
    }

}
