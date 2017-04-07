package com.baidu.unbiz.fluentvalidator;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.unbiz.fluentvalidator.able.ListAble;
import com.baidu.unbiz.fluentvalidator.able.ToStringable;
import com.baidu.unbiz.fluentvalidator.annotation.NotThreadSafe;
import com.baidu.unbiz.fluentvalidator.annotation.Stateful;
import com.baidu.unbiz.fluentvalidator.exception.RuntimeValidateException;
import com.baidu.unbiz.fluentvalidator.registry.Registry;
import com.baidu.unbiz.fluentvalidator.registry.impl.SimpleRegistry;
import com.baidu.unbiz.fluentvalidator.support.GroupingHolder;
import com.baidu.unbiz.fluentvalidator.util.ArrayUtil;
import com.baidu.unbiz.fluentvalidator.util.CollectionUtil;
import com.baidu.unbiz.fluentvalidator.util.Function;
import com.baidu.unbiz.fluentvalidator.util.Preconditions;
import com.baidu.unbiz.fluentvalidator.util.ReflectionUtil;
import com.baidu.unbiz.fluentvalidator.validator.element.IterableValidatorElement;
import com.baidu.unbiz.fluentvalidator.validator.element.MultiValidatorElement;
import com.baidu.unbiz.fluentvalidator.validator.element.ValidatorElement;
import com.baidu.unbiz.fluentvalidator.validator.element.ValidatorElementList;

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
 * </pre>
 */
@NotThreadSafe
@Stateful
public class FluentValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(FluentValidator.class);

    /**
     * 验证器链，惰性求值期间就是不断的改变这个链表，及时求值期间就是遍历链表依次执行验证
     */
    private ValidatorElementList validatorElementList = new ValidatorElementList();

    /**
     * 是否一旦发生验证错误就退出，默认为true
     *
     * @see #failFast()
     * @see #failOver()
     */
    private boolean isFailFast = true;

    /**
     * 验证器上下文
     * <p/>
     * 该<tt>context</tt>可以在所有验证器间共享数据
     */
    private ValidatorContext context = new ValidatorContext();

    /**
     * 验证结果，仅内部使用，外部使用验证结果需要使用{@link #result(ResultCollector)}来做收殓处理
     */
    private ValidationResult result = new ValidationResult();

    /**
     * 默认验证回调
     */
    protected ValidateCallback defaultCb = new DefaultValidateCallback();

    /**
     * 如果启用通过{@link com.baidu.unbiz.fluentvalidator.annotation.FluentValidate}注解方式的验证，需要寻找验证器实例，这里为注册中心
     * <p/>
     * 通过{@link #configure(Registry)}来配置
     */
    private Registry registry = new SimpleRegistry();

    /**
     * 记录上一次添加的验证器数量，用于{@link #when(boolean)}做判断条件是否做当前验证
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
        return checkAll(null);
    }

    /**
     * Groupings分组，有两个用途：
     * <ul>
     * <li>1. 当启用注解声明式验证时，用于区分是否做某次校验</li>
     * <li>1. 当Hibernate Validator时，含义和该框架内部的分组grouping相同</li>
     * </ul>
     */
    private Class<?>[] groups;

    /**
     * 排除的Groupings分组，当启用注解声明式验证时，用于区分是否做某次校验
     */
    private Class<?>[] excludeGroups;

    /**
     * 创建<tt>FluentValidator</tt>
     *
     * @param groups 分组
     *
     * @return FluentValidator
     */
    public static FluentValidator checkAll(Class... groups) {
        return new FluentValidator().setGroups(groups);
    }

    /**
     * 使用已经存在的一个验证上下文，共享context本身以及验证结果
     *
     * @param context 验证上下文
     *
     * @return FluentValidator
     */
    public FluentValidator withContext(ValidatorContext context) {
        this.context = context;
        this.result = context.result;
        return this;
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
        MultiValidatorElement multiValidatorElement = doOn(t);
        LOGGER.debug(multiValidatorElement + " will be performed");
        lastAddCount = multiValidatorElement.size();
        return this;
    }

    /**
     * 在某个数组对象上通过{@link com.baidu.unbiz.fluentvalidator.annotation.FluentValidate}注解方式的验证，
     * 需要保证{@link #configure(Registry)}已经先执行配置完毕<code>Registry</code>
     * <p/>
     * 注：当数组为空时，则会跳过
     *
     * @param t 待验证对象
     *
     * @return FluentValidator
     */
    public <T> FluentValidator onEach(T[] t) {
        if (ArrayUtil.isEmpty(t)) {
            lastAddCount = 0;
            return this;
        }

        return onEach(Arrays.asList(t));
    }

    /**
     * 在某个集合对象上通过{@link com.baidu.unbiz.fluentvalidator.annotation.FluentValidate}注解方式的验证，
     * 需要保证{@link #configure(Registry)}已经先执行配置完毕<code>Registry</code>
     * <p/>
     * 注：当集合为空时，则会跳过
     *
     * @param t 待验证对象
     *
     * @return FluentValidator
     */
    public <T> FluentValidator onEach(Collection<T> t) {
        if (CollectionUtil.isEmpty(t)) {
            lastAddCount = 0;
            return this;
        }

        MultiValidatorElement multiValidatorElement = null;
        for (T element : t) {
            multiValidatorElement = doOn(element);
            lastAddCount += multiValidatorElement.size();
        }
        LOGGER.debug(
                String.format("Total %d of %s will be performed", t.size(), multiValidatorElement));
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
    //TODO That would be much more easier if leveraging Java8 lambda feature
    protected <T> MultiValidatorElement doOn(final T t) {
        if (registry == null) {
            throw new RuntimeValidateException("When annotation-based validation enabled, one must use configure"
                    + "(Registry) method to let FluentValidator know where to search and get validator instances");
        }
        List<AnnotationValidator> anntValidatorsOfAllFields =
                AnnotationValidatorCache.getAnnotationValidator(registry, t);
        if (CollectionUtil.isEmpty(anntValidatorsOfAllFields)) {
            // no field configured with annotation
            return new MultiValidatorElement(Collections.EMPTY_LIST);
        }

        List<ValidatorElement> elementList = CollectionUtil.createArrayList();
        for (final AnnotationValidator anntValidatorOfOneField : anntValidatorsOfAllFields) {
            Object realTarget = ReflectionUtil.invokeMethod(anntValidatorOfOneField.getMethod(), t);

            if (!CollectionUtil.isEmpty(anntValidatorOfOneField.getValidators())) {
                if (!ArrayUtil.hasIntersection(anntValidatorOfOneField.getGroups(), groups)) {
                    // groups have no intersection
                    LOGGER.debug(String.format("Current groups: %s not match %s", Arrays.toString(groups),
                            anntValidatorOfOneField));
                    continue;
                }

                if (!ArrayUtil.isEmpty(excludeGroups)) {
                    if (ArrayUtil.hasIntersection(anntValidatorOfOneField.getGroups(), excludeGroups)) {
                        LOGGER.debug(String.format("Current groups: %s will be ignored because you specify %s",
                                Arrays.toString(
                                        excludeGroups), anntValidatorOfOneField));
                        continue;
                    }
                }

                for (final Validator v : anntValidatorOfOneField.getValidators()) {
                    elementList.add(new ValidatorElement(realTarget, v, new ToStringable() {
                        @Override
                        public String toString() {
                            return String.format("%s#%s@%s", t.getClass().getSimpleName(),
                                    anntValidatorOfOneField.getField().getName(), v);
                        }
                    }));
                }
            }

            // cascade handle
            if (anntValidatorOfOneField.isCascade()) {
                Field field = anntValidatorOfOneField.getField();
                if (Collection.class.isAssignableFrom(field.getType())) {
                    onEach((Collection) realTarget);
                } else if (field.getType().isArray()) {
                    onEach(ArrayUtil.toWrapperIfPrimitive(realTarget));
                } else {
                    on(realTarget);
                }
            }
        }
        MultiValidatorElement m = new MultiValidatorElement(elementList);
        validatorElementList.add(m);
        return m;
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
        composeIfPossible(v, t);
        doAdd(new ValidatorElement(t, v));
        lastAddCount = 1;
        return this;
    }

    
    public <T> FluentValidator on(T t, Validator<T> v,String message) {
        Preconditions.checkNotNull(v, "Validator should not be NULL");
        composeIfPossible(v, t);
        doAdd(new ValidatorElement(t, v, message));
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
        Preconditions.checkNotNull(chain, "ValidatorChain should not be NULL");
        final FluentValidator self = this;
        if (CollectionUtil.isEmpty(chain.getValidators())) {
            lastAddCount = 0;
        } else {
            for (Validator v : chain.getValidators()) {
                composeIfPossible(v, t);
                doAdd(new ValidatorElement(t, v));
            }
            lastAddCount = chain.getValidators().size();
        }

        return this;
    }

    /**
     * 在待验证对象数组<tt>t</tt>上，使用<tt>v</tt>验证器进行验证
     * <p/>
     * 注：当数组为空时，则会跳过
     *
     * @param t 待验证对象数组
     * @param v 验证器
     *
     * @return FluentValidator
     */
    public <T> FluentValidator onEach(T[] t, final Validator<T> v) {
        Preconditions.checkNotNull(v, "Validator should not be NULL");
        if (ArrayUtil.isEmpty(t)) {
            lastAddCount = 0;
            return this;
        }

        return onEach(Arrays.asList(t), v);
    }

    /**
     * 在待验证对象集合<tt>t</tt>上，使用<tt>v</tt>验证器进行验证
     * <p/>
     * 注：当集合为空时，则会跳过
     *
     * @param t 待验证对象集合
     * @param v 验证器
     *
     * @return FluentValidator
     */
    public <T> FluentValidator onEach(Collection<T> t, final Validator<T> v) {
        Preconditions.checkNotNull(v, "Validator should not be NULL");
        if (CollectionUtil.isEmpty(t)) {
            lastAddCount = 0;
        } else {
            List<ValidatorElement> elementList = CollectionUtil.transform(t, new Function<T, ValidatorElement>() {
                @Override
                public ValidatorElement apply(T elem) {
                    composeIfPossible(v, elem);
                    return new ValidatorElement(elem, v);
                }
            });
            lastAddCount = t.size();
            doAdd(new IterableValidatorElement(elementList));
        }

        return this;
    }

    /**
     * 将验证对象及其验证器放入{@link #validatorElementList}中
     *
     * @param listAble 验证对象及其验证器封装类
     */
    protected void doAdd(ListAble<ValidatorElement> listAble) {
        validatorElementList.add(listAble);
        LOGGER.debug(listAble + " will be performed");
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

    public FluentValidator doCustomValidate() {
        return doCustomValidate(defaultCb);
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
            LOGGER.debug("Nothing to validate");
            return this;
        }
        context.setResult(result);

        LOGGER.debug("Start to validate through " + validatorElementList);
        long start = System.currentTimeMillis();
        try {
            GroupingHolder.setGrouping(groups);
            for (ValidatorElement element : validatorElementList.getAllValidatorElements()) {
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
            GroupingHolder.clean();
            int timeElapsed = (int) (System.currentTimeMillis() - start);
            LOGGER.debug("End to validate through" + validatorElementList + " costing " + timeElapsed + "ms with "
                    + "isSuccess=" + result.isSuccess());
            result.setTimeElapsed(timeElapsed);
        }
        return this;
    }

    
    public FluentValidator doCustomValidate(ValidateCallback cb) {
        Preconditions.checkNotNull(cb, "ValidateCallback should not be NULL");
        if (validatorElementList.isEmpty()) {
            LOGGER.debug("Nothing to validate");
            return this;
        }
        context.setResult(result);

        LOGGER.debug("Start to validate through " + validatorElementList);
        long start = System.currentTimeMillis();
        try {
            GroupingHolder.setGrouping(groups);
            for (ValidatorElement element : validatorElementList.getAllValidatorElements()) {
                Object target = element.getTarget();
                Validator v = element.getValidator();
                String message = element.getMessage();
                try {
                    if (v.accept(context, target)) {
                        if (!v.validate(context, target,message)) {
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
            GroupingHolder.clean();
            int timeElapsed = (int) (System.currentTimeMillis() - start);
            LOGGER.debug("End to validate through" + validatorElementList + " costing " + timeElapsed + "ms with "
                    + "isSuccess=" + result.isSuccess());
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

    /**
     * 设置分组
     *
     * @param groups 分组
     *
     * @return FluentValidator
     */
    public FluentValidator setGroups(Class<?>[] groups) {
        this.groups = groups;
        return this;
    }

    /**
     * 设置是否快速失败
     *
     * @param isFailFast 是否快速失败
     *
     * @return FluentValidator
     */
    public FluentValidator setIsFailFast(boolean isFailFast) {
        this.isFailFast = isFailFast;
        return this;
    }

    /**
     * 设置排除的分组
     *
     * @param excludeGroups 排除分组
     *
     * @return FluentValidator
     */
    public FluentValidator setExcludeGroups(Class<?>[] excludeGroups) {
        this.excludeGroups = excludeGroups;
        return this;
    }

    /**
     * 如果验证器是一个{@link ValidatorHandler}实例，那么可以通过{@link ValidatorHandler#compose(FluentValidator, ValidatorContext, Object)}
     * 方法增加一些验证逻辑
     *
     * @param v 验证器
     * @param t 待验证对象
     */
    private <T>  void composeIfPossible(Validator<T> v, T t) {
        final FluentValidator self = this;
        if (v instanceof ValidatorHandler) {
            ((ValidatorHandler) v).compose(self, context, t);
        }
    }
}