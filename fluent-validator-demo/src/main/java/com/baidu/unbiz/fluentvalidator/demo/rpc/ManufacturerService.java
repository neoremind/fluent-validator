package com.baidu.unbiz.fluentvalidator.demo.rpc;

import java.util.List;

/**
 * @author zhangxu
 */
public interface ManufacturerService {

    List<String> getAllManufacturers();

    void setIsMockFail(boolean isMockFail);

}
