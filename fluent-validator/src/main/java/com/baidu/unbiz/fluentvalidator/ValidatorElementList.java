package com.baidu.unbiz.fluentvalidator;

import java.util.LinkedList;

/**
 * @author zhangxu
 */
class ValidatorElementList {

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
