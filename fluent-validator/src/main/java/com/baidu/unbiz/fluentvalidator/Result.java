package com.baidu.unbiz.fluentvalidator;

import java.util.ArrayList;
import java.util.List;

/**
 * 验证结果存根
 *
 * @author zhangxu
 */
public class Result {

    /**
     * 错误消息列表
     */
    private List<String> errorMsgs;

    @Override
    public String toString() {
        return String.format("Result{hasError=%s, errorMsgs=%s}", hasError(), errorMsgs);
    }

    /**
     * 获取错误数量
     *
     * @return 错误数量
     */
    public int getErrorNumber() {
        return errorMsgs == null ? 0 : errorMsgs.size();
    }

    /**
     * 是否存在错误
     *
     * @return 是否存在错误
     */
    public boolean hasNoError() {
        return errorMsgs == null || errorMsgs.size() == 0;
    }

    /**
     * 是否存在错误
     *
     * @return 是否存在错误
     *
     * @see #hasNoError()
     */
    public boolean hasError() {
        return !hasNoError();
    }

    /**
     * 获取所有错误
     *
     * @return 错误消息列表
     */
    public List<String> getErrorMsgs() {
        return errorMsgs;
    }

    /**
     * 添加错误信息
     *
     * @param msg 错误信息
     */
    public void addErrorMsg(String msg) {
        if (errorMsgs == null) {
            errorMsgs = new ArrayList<String>(4);
        }
        errorMsgs.add(msg);
    }

}
