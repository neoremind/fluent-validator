package com.baidu.unbiz.fluentvalidator;

import com.baidu.unbiz.fluentvalidator.util.Decorator;


/**
 * Quick validator for quickly executing template code like below:
 * <pre>
 * Result ret = FluentValidator.checkAll().configure(new SimpleRegistry())
 *         .on(car)
 *         .doValidate()
 *         .result(toSimple());
 * </pre>
 *
 * @author zhangxu
 */
public class QuickValidator {

    /**
     * Execute validation by using a new FluentValidator instance with a shared context.
     * The result type is {@link ComplexResult}
     *
     * @param decorator Same as decorator design pattern, provided to add more functions to the fluentValidator
     * @param context   Validation context which can be shared
     * @return ComplexResult
     */
    public static ComplexResult doAndGetComplexResult(Decorator decorator, ValidatorContext context) {
        return validate(decorator, FluentValidator.checkAll(), context, ResultCollectors.toComplex());
    }

    /**
     * Execute validation by using a new FluentValidator instance without a shared context.
     * The result type is {@link ComplexResult}
     *
     * @param decorator Same as decorator design pattern, provided to add more functions to the fluentValidator
     * @param context   Validation context which can be shared
     * @return ComplexResult
     */
    public static ComplexResult doAndGetComplexResult(Decorator decorator) {
        return validate(decorator, FluentValidator.checkAll(), null, ResultCollectors.toComplex());
    }

    /**
     * Execute validation by using a new FluentValidator instance with a shared context.
     * The result type is {@link ComplexResult2}
     *
     * @param decorator Same as decorator design pattern, provided to add more functions to the fluentValidator
     * @param context   Validation context which can be shared
     * @return ComplexResult2
     */
    public static ComplexResult2 doAndGetComplexResult2(Decorator decorator, ValidatorContext context) {
        return validate(decorator, FluentValidator.checkAll(), context, ResultCollectors.toComplex2());
    }

    /**
     * Execute validation by using a new FluentValidator instance and without a shared context.
     * The result type is {@link ComplexResult2}
     *
     * @param decorator Same as decorator design pattern, provided to add more functions to the fluentValidator
     * @return ComplexResult2
     */
    public static ComplexResult2 doAndGetComplexResult2(Decorator decorator) {
        return validate(decorator, FluentValidator.checkAll(), null, ResultCollectors.toComplex2());
    }

    /**
     * Use the <code>decorator</code> to add or attach more functions the given <code>fluentValidator</code> instance.
     * <p>
     * The context can be shared and set up in the new FluentValidator instance.
     * <p>
     * The motivation for this method is to provide a quick way to launch a validation task. By just passing
     * the validation logic which is wrapped inside the decorator, users can do validation in a ease way.
     * <p>
     * Because Java7 lacks the ability to pass "action" to a method, so there is {@link Decorator} to help
     * to achieve a functional programming approach to get it done. In Java8, users can replace it with lambda.
     *
     * @param decorator       Same as decorator design pattern, provided to add more functions to the fluentValidator
     * @param fluentValidator The base fluentValidator to be executed upon
     * @param context         Validation context which can be shared
     * @param resultCollector Result collector
     * @return Result
     */
    public static <T extends GenericResult<ValidationError>> T validate(Decorator decorator, FluentValidator fluentValidator, ValidatorContext context, ResultCollector<T> resultCollector) {
        FluentValidator fv = decorator.decorate(fluentValidator);
        if (context != null) {
            fv.withContext(context);
        }

        T localResult = fv.failOver()
                .doValidate()
                .result(resultCollector);

        return localResult;
    }

}
