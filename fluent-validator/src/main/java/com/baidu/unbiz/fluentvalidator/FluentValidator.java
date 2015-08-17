package com.baidu.unbiz.fluentvalidator;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.unbiz.fluentvalidator.annotation.Stateful;
import com.baidu.unbiz.fluentvalidator.annotation.NotThreadSafe;
import com.baidu.unbiz.fluentvalidator.exception.RuntimeValidateException;
import com.baidu.unbiz.fluentvalidator.registry.Registry;
import com.baidu.unbiz.fluentvalidator.util.CollectionUtil;
import com.baidu.unbiz.fluentvalidator.util.Preconditions;
import com.baidu.unbiz.fluentvalidator.util.ReflectionUtil;

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
 *     .doValidate().result(toSimple());
 * System.out.println(ret);
 * </pre>
 */
@NotThreadSafe
@Stateful
public class FluentValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(FluentValidator.class);

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
     * 验证结果
     */
    private ValidationResult result = new ValidationResult();

    /**
     * 默认验证回调
     */
    protected ValidateCallback defaultCb = new DefaulValidateCallback();

    /**
     * 如果启用通过{@link com.baidu.unbiz.fluentvalidator.annotation.FluentValidate}注解方式的验证，需要寻找验证器实例，这里为注册中心
     */
    private Registry registry;

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
     * 如果启用通过{@link com.baidu.unbiz.fluentvalidator.annotation.FluentValidate}注解方式的验证，需要寻找验证器实例，这里配置注册中心的步骤
     *
     * @param registry 验证器注册查找器
     *
     * @return FluentValidator
     */
    public FluentValidator configure(Registry registry) {
        Preconditions.checkNotNull(registry, "Registry should not be NULL");
        this.registry = registry;
        return this;
    }

    /**
     * 在某个对象上通过{@link com.baidu.unbiz.fluentvalidator.annotation.FluentValidate}注解方式的验证，
     * 需要保证{@link #configure(Registry)}已经先执行配置完毕<code>Registry</code>
     *
     * @param t 待验证对象
     *
     * @return FluentValidator
     */
    public <T> FluentValidator on(T t) {
        Set<String> logSet = new LinkedHashSet<String>();
        lastAddCount = doOn(t, logSet);
        if (!CollectionUtil.isEmpty(logSet)) {
            for (String log : logSet) {
                LOGGER.debug(log);
            }
        }
        return this;
    }

    /**
     * 在某个数组对象上通过{@link com.baidu.unbiz.fluentvalidator.annotation.FluentValidate}注解方式的验证，
     * 需要保证{@link #configure(Registry)}已经先执行配置完毕<code>Registry</code>
     *
     * @param t 待验证对象
     *
     * @return FluentValidator
     */
    public <T> FluentValidator onEach(T[] t) {
        Preconditions.checkNotNull(t, "Array should not be NULL");
        if (t.length == 0) {
            return this;
        }

        Set<String> logSet = new LinkedHashSet<String>();
        for (T element : t) {
            lastAddCount += doOn(element, logSet);
        }
        if (!CollectionUtil.isEmpty(logSet)) {
            for (String log : logSet) {
                LOGGER.debug(log);
            }
        }
        LOGGER.debug("To be validated array size is " + t.length);
        return this;
    }

    /**
     * 在某个集合对象上通过{@link com.baidu.unbiz.fluentvalidator.annotation.FluentValidate}注解方式的验证，
     * 需要保证{@link #configure(Registry)}已经先执行配置完毕<code>Registry</code>
     *
     * @param t 待验证对象
     *
     * @return FluentValidator
     */
    public <T> FluentValidator onEach(Collection<T> t) {
        Preconditions.checkNotNull(t, "Collection should not be NULL");
        if (t.size() == 0) {
            return this;
        }

        Set<String> logSet = new LinkedHashSet<String>();
        for (T element : t) {
            lastAddCount += doOn(element, logSet);
        }
        if (!CollectionUtil.isEmpty(logSet)) {
            for (String log : logSet) {
                LOGGER.debug(log);
            }
        }
        LOGGER.debug("To be validated collection size is " + t.size());
        return this;
    }

    /**
     * 在某个对象上通过{@link com.baidu.unbiz.fluentvalidator.annotation.FluentValidate}注解方式的验证，
     * 需要保证{@link #configure(Registry)}已经先执行配置完毕<code>Registry</code>
     *
     * @param t      待验证对象
     * @param logSet 记录日志的set
     *
     * @return FluentValidator
     */
    private <T> int doOn(T t, Set<String> logSet) {
        if (registry == null) {
            throw new RuntimeValidateException("When annotation-based validation enabled, one must use configure"
                    + "(Registry) method to let FluentValidator know where to search and get validator instances");
        }
        int tmpLastAddCount = 0;
        List<AnnotationValidator> anntValidators = AnnotationValidatorCache.getAnnotationValidator(registry, t);
        if (!CollectionUtil.isEmpty(anntValidators)) {
            for (AnnotationValidator anntValidator : anntValidators) {
                if (!CollectionUtil.isEmpty(anntValidator.getValidators())) {
                    logSet.add(String.format("%s#%s on %s will be performed", t.getClass().getSimpleName(),
                            anntValidator.getField().getName(), anntValidator));
                    for (Validator v : anntValidator.getValidators()) {
                        Object realTarget = ReflectionUtil.invokeMethod(anntValidator.getMethod(), t);
                        validatorElementList.getList().add(new ValidatorElement(realTarget, v));
                        tmpLastAddCount++;
                    }
                }
            }
        }
        return tmpLastAddCount;
    }

    /**
     * 在待验证对象数组<tt>t</tt>上，使用<tt>v</tt>验证器进行验证
     *
     * @param t 待验证对象数组
     * @param v 验证器
     *
     * @return FluentValidator
     */
    public <T> FluentValidator onEach(T[] t, Validator<T> v) {
        Preconditions.checkNotNull(v, "Validator should not be NULL");
        Preconditions.checkNotNull(t, "Array should not be NULL");
        if (t.length == 0) {
            return this;
        }

        for (T element : t) {
            doOn(element, v);
        }
        lastAddCount = t.length;
        LOGGER.debug(String.format("%s on %s will be performed %d times",
                t.getClass().getSimpleName(),
                v, t.length));
        return this;
    }

    /**
     * 在待验证对象集合<tt>t</tt>上，使用<tt>v</tt>验证器进行验证
     *
     * @param t 待验证对象集合
     * @param v 验证器
     *
     * @return FluentValidator
     */
    public <T> FluentValidator onEach(Collection<T> t, Validator<T> v) {
        Preconditions.checkNotNull(v, "Validator should not be NULL");
        Preconditions.checkNotNull(t, "Collection should not be NULL");
        if (CollectionUtil.isEmpty(t)) {
            return this;
        }

        for (T element : t) {
            doOn(element, v);
        }
        lastAddCount = t.size();
        LOGGER.debug(String.format("%s on %s will be performed %d times",
                t.getClass().getSimpleName(),
                v, t.size()));
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
        Preconditions.checkNotNull(v, "Validator should not be NULL");
        doOn(t, v);
        lastAddCount = 1;
        LOGGER.debug(String.format("%s on %s will be performed", t.getClass().getSimpleName(), v));
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
        Preconditions.checkNotNull(chain, "ValidatorChain should not be NULL");
        if (CollectionUtil.isEmpty(chain.getValidators())) {
            lastAddCount = 0;
        } else {
            for (Validator v : chain.getValidators()) {
                doOn(t, v);
            }
            lastAddCount = chain.getValidators().size();
            LOGGER.debug(String.format("%s on %s will be performed", t.getClass().getSimpleName(), chain));
        }
        return this;
    }

    /**
     * 内部使用的验证单个元素的方法，供以下方法作为模板使用
     * <ul>
     * <li>{@link #on(Object, Validator)}</li>
     * <li>{@link #on(Object, ValidatorChain)}</li>
     * <li>{@link #onEach(Collection, Validator)}</li>
     * <li>{@link #onEach(Object[], Validator)}</li>
     * </ul>
     *
     * @param t 待验证对象
     * @param v 验证器
     */
    private <T> void doOn(T t, Validator<T> v) {
        validatorElementList.getList().add(new ValidatorElement(t, v));
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
     * 按照默认验证回调条件，开始使用验证
     *
     * @return FluentValidator
     */
    public FluentValidator doValidate() {
        return doValidate(defaultCb);
    }

    /**
     * 按照指定验证回调条件，开始使用验证
     *
     * @param cb 验证回调
     *
     * @return FluentValidator
     *
     * @see ValidateCallback
     */
    public FluentValidator doValidate(ValidateCallback cb) {
        Preconditions.checkNotNull(cb, "ValidateCallback should not be NULL");
        if (validatorElementList.isEmpty()) {
            return this;
        }
        context.setResult(result);

        LOGGER.debug("Start to validate through " + validatorElementList);
        long start = System.currentTimeMillis();
        try {
            for (ValidatorElement element : validatorElementList.getList()) {
                Object target = element.getTarget();
                Validator v = element.getValidator();
                try {
                    if (v.accept(context, target)) {
                        if (!v.validate(context, target)) {
                            result.setIsSuccess(false);
                            if (isFailFast) {
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    try {
                        v.onException(e, context, target);
                        cb.onUncaughtException(v, e, target);
                    } catch (Exception e1) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.error(v + " onException or onUncaughtException throws exception due to " + e1
                                    .getMessage(), e1);
                        }
                        throw new RuntimeValidateException(e1);
                    }
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.error(v + " failed due to " + e.getMessage(), e);
                    }
                    throw new RuntimeValidateException(e);
                }
            }

            if (result.isSuccess()) {
                cb.onSuccess(validatorElementList);
            } else {
                cb.onFail(validatorElementList, result.getErrors());
            }
        } finally {
            int timeElapsed = (int) (System.currentTimeMillis() - start);
            LOGGER.debug("End to validate through" + validatorElementList + " costing " + timeElapsed + "ms");
            result.setTimeElapsed(timeElapsed);
        }
        return this;
    }

    /**
     * 转换为对外的验证结果，在<code>FluentValidator.on(..).on(..).doValidate()</code>这一连串“<a href="https://en.wikipedia
     * .org/wiki/Lazy_evaluation">惰性求值</a>”计算后的“及时求值”收殓出口。
     * <p/>
     * &lt;T&gt;是验证结果的泛型
     *
     * @param resultCollector 验证结果收集器
     *
     * @return 对外验证结果
     */
    public <T> T result(ResultCollector<T> resultCollector) {
        return resultCollector.toResult(result);
    }

}