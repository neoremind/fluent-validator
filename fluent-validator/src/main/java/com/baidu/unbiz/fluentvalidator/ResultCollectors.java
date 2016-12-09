package com.baidu.unbiz.fluentvalidator;

import com.baidu.unbiz.fluentvalidator.util.CollectionUtil;
import com.baidu.unbiz.fluentvalidator.util.Function;
import com.baidu.unbiz.fluentvalidator.util.Supplier;


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
            if (result.isSuccess()) {
                ret.setIsSuccess(true);
            } else {
                ret.setIsSuccess(false);
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
            return newComplexResult(new Supplier<ComplexResult>() {
                @Override
                public ComplexResult get() {
                    return new ComplexResult();
                }
            }, result);
        }
    }

    /**
     * 框架提供的一个复杂结果收集器，结果对于NULL友好，即使没有任何错误{@link ComplexResult2#errors}也不会是NULL，而是一个empty list
     */
    static class ComplexResult2CollectorImpl implements ResultCollector<ComplexResult2> {

        @Override
        public ComplexResult2 toResult(ValidationResult result) {
            return newComplexResult(new Supplier<ComplexResult2>() {
                @Override
                public ComplexResult2 get() {
                    return new ComplexResult2();
                }
            }, result);
        }
    }

    /**
     * {@link #toComplex()}和{@link #toComplex2()}复用的结果生成函数
     *
     * @param supplier 供给模板
     * @param result   内部用验证结果
     * @param <T>      结果的泛型
     * @return 结果
     */
    static <T extends ComplexResult> T newComplexResult(Supplier<T> supplier, ValidationResult result) {
        T ret = supplier.get();
        if (result.isSuccess()) {
            ret.setIsSuccess(true);
        } else {
            ret.setIsSuccess(false);
            ret.setErrors(result.getErrors());
        }

        ret.setTimeElapsed(result.getTimeElapsed());
        return ret;
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

    /**
     * 静态方法返回一个复杂结果收集器，结果对于NULL友好，即使没有任何错误{@link ComplexResult2#errors}也不会是NULL，而是一个empty list
     *
     * @return 简单的结果收集器<code>ComplexResult2CollectorImpl</code>
     */
    public static ResultCollector<ComplexResult2> toComplex2() {
        return new ComplexResult2CollectorImpl();
    }

}
