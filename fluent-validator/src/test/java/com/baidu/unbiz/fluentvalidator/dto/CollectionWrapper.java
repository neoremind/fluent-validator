package com.baidu.unbiz.fluentvalidator.dto;

import java.util.Map;

/**
 * 针对collection集合类的封装
 */
public class CollectionWrapper {

    private Map<Integer, Person> id2PersonMap;

    public Map<Integer, Person> getId2PersonMap() {
        return id2PersonMap;
    }

    public void setId2PersonMap(Map<Integer, Person> id2PersonMap) {
        this.id2PersonMap = id2PersonMap;
    }
}
