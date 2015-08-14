package com.baidu.unbiz.fluentvalidator;

import com.baidu.unbiz.fluentvalidator.util.CollectionUtil;
import com.baidu.unbiz.fluentvalidator.util.Function;

/**
 * 框架自身实现的一个简单的验证结果收集器
 *
 * @author zhangxu
 * @see ResultCollector
 * @see Result
 */
public class ResultCollectors {

    /**
     * 框架提供的一个简单结果收集器
     */
    static class SimpleResultCollectorImpl implements ResultCollector<Result> {

        @Override
        public Result toResult(ValidationResult result) {
            Result ret = new Result();
            if (result.hasError()) {
                ret.setErrors(CollectionUtil.transform(result.getErrors(), new Function<ValidationError, String>() {
                    @Override
                    public String apply(ValidationError input) {
                        return input.getErrorMsg();
                    }
                }));
            }

            return ret;
        }
    }

    /**
     * 框架提供的一个复杂结果收集器
     */
    static class ComplexResultCollectorImpl implements ResultCollector<ComplexResult> {

        @Override
        public ComplexResult toResult(ValidationResult result) {
            ComplexResult ret = new ComplexResult();
            if (result.hasError()) {
                ret.setErrors(result.getErrors());
            }

            ret.setTimeElapsed(result.getTimeElapsed());
            return ret;
        }
    }

    /**
     * 静态方法返回一个简单结果收集器
     *
     * @return 简单的结果收集器<code>ResultCollectorImpl</code>
     */
    public static ResultCollector<Result> toSimple() {
        return new SimpleResultCollectorImpl();
    }

    /**
     * 静态方法返回一个复杂结果收集器
     *
     * @return 简单的结果收集器<code>ComplexResultCollectorImpl</code>
     */
    public static ResultCollector<ComplexResult> toComplex() {
        return new ComplexResultCollectorImpl();
    }

}
