package com.baidu.unbiz.fluentvalidator;

/**
 * 链式调用验证器
 * <p/>
 * 按照<a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>风格实现的验证工具，以一种近似于可以语义解释的方式做对象的验证。
 * <p/>
 * 典型的调用方式如下：
 * <pre>
 * Result ret = FluentValidator.checkAll().failFast()
 *     .on(car.getLicensePlate(), new CarLicensePlateValidator())
 *     .on(car.getManufacturer(), new CarManufacturerValidator())
 *     .on(car.getSeatCount(), new CarSeatCountValidator())
 *     .doValidate();
 * System.out.println(ret);
 * </pre>
 */
@NotThreadSafe
public class FluentValidator {

    /**
     * 验证器链
     */
    private ValidatorElementList validatorElementList = new ValidatorElementList();

    /**
     * 是否一旦发生验证错误就退出，默认为true
     */
    private boolean isFailFast = true;

    /**
     * 验证器上下文
     * <p/>
     * 该<tt>context</tt>可以在所有验证器间共享
     */
    private ValidatorContext context = new ValidatorContext();

    /**
     * 记录上一次添加的验证器数量
     */
    private int lastAddCount = 0;

    /**
     * 将键值对放入上下文
     *
     * @param key   键
     * @param value 值
     *
     * @return FluentValidator
     */
    public FluentValidator putAttribute2Context(String key, Object value) {
        if (context == null) {
            context = new ValidatorContext();
        }
        context.setAttribute(key, value);
        return this;
    }

    /**
     * 将闭包注入上下文
     *
     * @param key   键
     * @param value 闭包
     *
     * @return FluentValidator
     */
    public FluentValidator putClosure2Context(String key, Closure value) {
        if (context == null) {
            context = new ValidatorContext();
        }
        context.setClosure(key, value);
        return this;
    }

    /**
     * 私有构造方法，只能通过{@link #checkAll()}去创建对象
     */
    protected FluentValidator() {
    }

    /**
     * 创建<tt>FluentValidator</tt>
     *
     * @return FluentValidator
     */
    public static FluentValidator checkAll() {
        return new FluentValidator();
    }

    /**
     * 出错即退出
     *
     * @return FluentValidator
     */
    public FluentValidator failFast() {
        this.isFailFast = true;
        return this;
    }

    /**
     * 出错不退出而继续
     *
     * @return FluentValidator
     */
    public FluentValidator failOver() {
        this.isFailFast = false;
        return this;
    }

    /**
     * 在待验证对象<tt>t</tt>上，使用<tt>v</tt>验证器进行验证
     *
     * @param t 待验证对象
     * @param v 验证器
     *
     * @return FluentValidator
     */
    public <T> FluentValidator on(T t, Validator<T> v) {
        validatorElementList.getList().add(new ValidatorElement(t, v));
        lastAddCount = 1;
        return this;
    }

    /**
     * 在待验证对象<tt>t</tt>上，使用<tt>chain</tt>验证器链进行验证
     *
     * @param t     待验证对象
     * @param chain 验证器链
     *
     * @return FluentValidator
     */
    public <T> FluentValidator on(T t, ValidatorChain chain) {
        if (chain.getValidators() == null || chain.getValidators().isEmpty()) {
            lastAddCount = 0;
            return this;
        }
        for (Validator v : chain.getValidators()) {
            validatorElementList.getList().add(new ValidatorElement(t, v));
        }
        lastAddCount = chain.getValidators().size();
        return this;
    }

    /**
     * 当满足<code>expression</code>条件时，才去使用前一个{@link Validator}或者{@link ValidatorChain}来验证
     *
     * @param expression 满足条件表达式
     *
     * @return FluentValidator
     */
    public FluentValidator when(boolean expression) {
        if (!expression) {
            for (int i = 0; i < lastAddCount; i++) {
                validatorElementList.getList().removeLast();
            }
        }
        return this;
    }

    /**
     * 默认验证回调
     */
    protected ValidateCallback defaultCb = new DefaulValidateCallback();

    /**
     * 按照默认验证回调条件，开始使用验证
     *
     * @return 结果对象
     */
    public Result doValidate() {
        return doValidate(defaultCb);
    }

    /**
     * 按照指定验证回调条件，开始使用验证
     *
     * @param cb 验证回调
     *
     * @return 结果对象
     *
     * @see ValidateCallback
     */
    public Result doValidate(ValidateCallback cb) {
        Result ret = new Result();
        if (validatorElementList.isEmpty()) {
            return ret;
        }
        context.setResult(ret);

        for (ValidatorElement element : validatorElementList.getList()) {
            Object target = element.getTarget();
            Validator v = element.getValidator();
            try {
                if (v.accept(context, target)) {
                    if (!v.validate(context, target)) {
                        if (isFailFast) {
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                v.onException(e, context, target);
                try {
                    cb.onUncaughtException(v, e, target);
                } catch (Exception e1) {
                    throw new RuntimeValidateException(e1);
                }
                throw new RuntimeValidateException(e);
            }
        }

        if (ret.hasNoError()) {
            cb.onSuccess(validatorElementList);
        } else {
            cb.onFail(validatorElementList, ret.getErrorMsgs());
        }
        return ret;
    }
}