package com.baidu.unbiz.fluentvalidator.validator;

import org.springframework.stereotype.Component;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.baidu.unbiz.fluentvalidator.error.CarError;

/**
 * @author zhangxu
 */
@Component
public class CarLicensePlateValidator extends ValidatorHandler<String> implements Validator<String> {

    private MockRpcService normalMockRpcService = new NormalMockRpcService();

    private MockRpcService abNormalMockRpcService = new AbnormalMockRpcService();

    @Override
    public boolean validate(ValidatorContext context, String t) {
        if (t.startsWith("NYC") || t.startsWith("LA") || t.startsWith("BEIJING")) {
            if (!normalMockRpcService.isValid(t)) {
                context.addErrorMsg(String.format(CarError.LICENSEPLATE_ERROR.msg(), t));
                return false;
            }
        } else {
            return abNormalMockRpcService.isValid(t);
        }
        return true;
    }

}

interface MockRpcService {

    boolean isValid(String licensePlate);

}

class NormalMockRpcService implements MockRpcService {

    public boolean isValid(String licensePlate) {
        if (licensePlate.startsWith("BEIJING")) {
            return false;
        }
        return true;
    }

}

class AbnormalMockRpcService implements MockRpcService {

    public boolean isValid(String licensePlate) {
        throw new RuntimeException("Call Rpc failed");
    }

}
