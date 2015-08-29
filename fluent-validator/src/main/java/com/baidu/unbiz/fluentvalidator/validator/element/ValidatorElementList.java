package com.baidu.unbiz.fluentvalidator.validator.element;

import java.util.LinkedList;
import java.util.List;

import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.able.ListAble;
import com.baidu.unbiz.fluentvalidator.util.CollectionUtil;

/**
 * 在{@link FluentValidator}内部调用使用的验证器链
 *
 * @author zhangxu
 */
public class ValidatorElementList {

    /**
     * 待验证对象及其验证器链表
     */
    private LinkedList<ListAble<ValidatorElement>> validatorElementLinkedList = CollectionUtil.createLinkedList();

    /**
     * 加入待验证对象及其验证器
     *
     * @param listAble 待验证对象及其验证器封装类
     */
    public void add(ListAble<ValidatorElement> listAble) {
        this.validatorElementLinkedList.add(listAble);
    }

    /**
     * 获取待验证对象及其验证器链表长度
     *
     * @return 联调长度
     */
    public int size() {
        return validatorElementLinkedList.size();
    }

    /**
     * 获取验证器链
     *
     * @return 验证器链
     */
    public LinkedList<ListAble<ValidatorElement>> getList() {
        return validatorElementLinkedList;
    }

    /**
     * 验证器链是否为空
     *
     * @return 是否为空
     */
    public boolean isEmpty() {
        return validatorElementLinkedList.isEmpty();
    }

    /**
     * 获取验证器链
     *
     * @return 验证器链
     */
    public List<ValidatorElement> getAllValidatorElements() {
        List<ValidatorElement> ret = CollectionUtil.createArrayList();
        for (ListAble<ValidatorElement> e : validatorElementLinkedList) {
            ret.addAll(e.getAsList());
        }
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (ListAble<ValidatorElement> e : validatorElementLinkedList) {
            if (count >= 20) {
                sb.append("[");
                sb.append(size() - count);
                sb.append("... more]->");
                break;
            }
            sb.append("[");
            sb.append(e);
            sb.append("]->");
            count++;
        }
        sb.append("NULL");
        return sb.toString();
    }

}
