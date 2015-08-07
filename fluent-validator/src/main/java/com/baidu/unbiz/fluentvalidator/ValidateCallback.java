package com.baidu.unbiz.fluentvalidator;

import java.util.List;

/**
 * 验证回调接口
 * <p/>
 * 以参数形式参与{@link FluentValidator#doValidate(ValidateCallback)}来做验证过程中的回调操作。
 *
 * @author zhangxu
 */
public interface ValidateCallback {

    /**
     * 所有验证完成并且成功后
     *
     * @param validatorElementList 验证器list
     */
    void onSuccess(ValidatorElementList validatorElementList);

    /**
     * 所有验证步骤结束，发现验证存在失败后
     *
     * @param validatorElementList 验证器list
     * @param errorMsgs            错误消息
     */
    void onFail(ValidatorElementList validatorElementList, List<String> errorMsgs);

    /**
     * 执行验证过程中发生了异常后
     *
     * @param validator 验证器
     * @param e         异常
     * @param target    正在验证的对象
     *
     * @throws Exception
     */
    void onUncaughtException(Validator validator, Exception e, Object target) throws Exception;

}
