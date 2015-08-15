package com.baidu.unbiz.fluentvalidator;

import java.util.LinkedList;

import com.baidu.unbiz.fluentvalidator.util.CollectionUtil;

/**
 * 在{@link FluentValidator}内部调用使用的验证器链
 *
 * @author zhangxu
 */
public class ValidatorElementList {

    /**
     * 验证器链
     */
    private LinkedList<ValidatorElement> list = CollectionUtil.createLinkedList();

    /**
     * 获取验证器链
     *
     * @return 验证器链
     */
    public LinkedList<ValidatorElement> getList() {
        return list;
    }

    /**
     * 简单验证器链是否为空
     *
     * @return 是否为空
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ValidatorElement element : list) {
            sb.append("[");
            sb.append(element.getTarget().getClass().getSimpleName());
            sb.append("@");
            sb.append(element.getValidator());
            sb.append("]->");
        }
        sb.append("NULL");
        return sb.toString();
    }

}
