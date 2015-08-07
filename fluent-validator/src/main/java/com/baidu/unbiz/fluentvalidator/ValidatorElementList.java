package com.baidu.unbiz.fluentvalidator;

import java.util.LinkedList;

/**
 * 在{@link FluentValidator}内部调用使用的验证器链
 *
 * @author zhangxu
 */
public class ValidatorElementList {

    /**
     * 验证器链
     */
    private LinkedList<ValidatorElement> list = new LinkedList<ValidatorElement>();

    public LinkedList<ValidatorElement> getList() {
        return list;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ValidatorElement element : list) {
            sb.append(element.getValidator()).append("->");
        }
        sb.append("NULL");
        return sb.toString();
    }
}
