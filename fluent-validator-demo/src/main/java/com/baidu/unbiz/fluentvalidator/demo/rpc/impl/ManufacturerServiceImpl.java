package com.baidu.unbiz.fluentvalidator.demo.rpc.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baidu.unbiz.fluentvalidator.demo.exception.RpcException;
import com.baidu.unbiz.fluentvalidator.demo.rpc.ManufacturerService;
import com.baidu.unbiz.fluentvalidator.util.CollectionUtil;

/**
 * @author zhangxu
 */
@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManufacturerServiceImpl.class);

    private boolean isMockFail = false;

    @Override
    public List<String> getAllManufacturers() {
        LOGGER.info("Call rpc to get all manufacturers...");
        if (isMockFail) {
            isMockFail = false;
            throw new RpcException("Get all manufacturers failed");
        }
        List<String> ret = CollectionUtil.createArrayList(3);
        ret.add("BMW");
        ret.add("Benz");
        ret.add("Chevrolet");
        return ret;
    }

    @Override
    public void setIsMockFail(boolean isMockFail) {
        this.isMockFail = isMockFail;
    }

}
