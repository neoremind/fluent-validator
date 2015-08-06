package com.baidu.unbiz.fluentvalidator.rpc.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.unbiz.fluentvalidator.rpc.ManufacturerService;

/**
 * @author zhangxu
 */
public class ManufacturerServiceImpl implements ManufacturerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManufacturerServiceImpl.class);

    @Override
    public List<String> getAllManufacturers() {
        LOGGER.info("Get all manufacturers");
        List<String> ret = new ArrayList<String>();
        ret.add("BMW");
        ret.add("Benz");
        ret.add("Chevrolet");
        return ret;
    }

}
