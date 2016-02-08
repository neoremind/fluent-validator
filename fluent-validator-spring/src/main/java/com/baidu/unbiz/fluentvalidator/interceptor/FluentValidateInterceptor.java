package com.baidu.unbiz.fluentvalidator.interceptor;

import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toComplex;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.DefaultValidateCallback;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ValidateCallback;
import com.baidu.unbiz.fluentvalidator.ValidatorChain;
import com.baidu.unbiz.fluentvalidator.annotation.FluentValid;
import com.baidu.unbiz.fluentvalidator.exception.RuntimeValidateException;
import com.baidu.unbiz.fluentvalidator.jsr303.HibernateSupportedValidator;
import com.baidu.unbiz.fluentvalidator.registry.impl.SpringApplicationContextRegistry;
import com.baidu.unbiz.fluentvalidator.support.FluentValidatorPostProcessor;
import com.baidu.unbiz.fluentvalidator.util.ArrayUtil;
import com.baidu.unbiz.fluentvalidator.util.CollectionUtil;
import com.baidu.unbiz.fluentvalidator.util.LocaleUtil;
import com.baidu.unbiz.fluentvalidator.util.Preconditions;
import com.baidu.unbiz.fluentvalidator.util.ReflectionUtil;

/**
 * 与Spring集成的拦截器，结合{@link FluentValid}注解装饰在参数前面，可以利用AOP来拦截请求，对参数进行校验
 *
 * @author zhangxu
 */
public class FluentValidateInterceptor implements MethodInterceptor, InitializingBean, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(FluentValidateInterceptor.class);

    /**
     * Spring容器上下文
     */
    private ApplicationContext applicationContext;

    /**
     * 验证回调
     */
    private ValidateCallback callback = new DefaultValidateCallback();

    /**
     * 验证器查找registry
     */
    private SpringApplicationContextRegistry registry;

    /**
     * FluentValidator后置处理
     * <p/>
     * 一般情况用不到，除非需要自己在FluentValidator中加入一些操作，例如
     * {@link FluentValidator#putAttribute2Context(String, Object)}
     * <p/>
     * 这里是做扩展使用的
     */
    private FluentValidatorPostProcessor fluentValidatorPostProcessor;

    /**
     * hibernate validator
     */
    private Validator validator;

    /**
     * 语言地区，主要为Hibernate Validator使用
     */
    private String locale;

    /**
     * 如果是hibernate validator验证注解的错误，统一存在一个error code
     * <p/>
     * 如果是{@link com.baidu.unbiz.fluentvalidator.Validator}的则可以自定义error code
     */
    private int hibernateDefaultErrorCode;

    @Override
    public void afterPropertiesSet() throws Exception {
        registry = new SpringApplicationContextRegistry();
        registry.setApplicationContext(applicationContext);

        // 如果hibernate validator通过spring配置了，则不进行初始化
        if (validator == null) {
            // init hibernate validator
            if (locale != null) {
                Locale.setDefault(LocaleUtil.parseLocale(locale));
            }
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }

        LOGGER.info("Place " + this.getClass().getSimpleName() + " as an interceptor");
    }

    /**
     * 拦截方法进行验证，如果参数前面存在{@link FluentValid}注解，则进行校验，分一下三中情况：
     * <ul>
     * <li>1. 普通对象，直接验证</li>
     * <li>2. 列表对象，onEach验证</li>
     * <li>3. 数组对象，onEach验证</li>
     * </ul>
     * <p/>
     * 注：<br/>
     * 1) 如果参数上的注解{@link FluentValid}设置了value，则value上的验证器先验证。
     * 2) hibernate validator次先校验，然后才是级联验证
     *
     * @param invocation 调用
     *
     * @return 返回对象
     *
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // Work out the target class: may be <code>null</code>.
        // The TransactionAttributeSource should be passed the target class
        // as well as the method, which may be from an interface.
        Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);
        Preconditions.checkState(targetClass != null, "Target class should NOT be NULL!");

        try {
            Object[] arguments = invocation.getArguments();
            Class<?>[] parameterTypes = invocation.getMethod().getParameterTypes();
            Method implMethod = ReflectionUtil.getMethod(targetClass, invocation.getMethod().getName(), parameterTypes);
            Annotation[][] paramAnnotations = implMethod.getParameterAnnotations();
            if (paramAnnotations != null) {
                start:
                for (int i = 0; i < paramAnnotations.length; i++) {
                    Annotation[] paramAnnotation = paramAnnotations[i];
                    if (ArrayUtil.isEmpty(paramAnnotation)) {
                        continue start;
                    }
                    for (int j = 0; j < paramAnnotation.length; j++) {
                        if (paramAnnotation[j].annotationType() == FluentValid.class) {
                            LOGGER.debug("Find @FluentValid annotation on index[" + i + "] parameter and ready to "
                                    + "validate for method " + implMethod);
                            ValidatorChain addOnValidatorChain =
                                    getAddOnValidatorChain((FluentValid) paramAnnotation[j]);
                            Class<?>[] groups = ((FluentValid) paramAnnotation[j]).groups();
                            Class<?>[] excludeGroups = ((FluentValid) paramAnnotation[j]).excludeGroups();
                            boolean isFailFast = ((FluentValid) paramAnnotation[j]).isFailFast();

                            FluentValidator fluentValidator = FluentValidator.checkAll(groups)
                                    .setExcludeGroups(excludeGroups)
                                    .configure(registry)
                                    .setIsFailFast(isFailFast);
                            if (fluentValidatorPostProcessor != null) {
                                fluentValidatorPostProcessor.postProcessBeforeDoValidate(fluentValidator, invocation);
                            }
                            ComplexResult result;
                            if (Collection.class.isAssignableFrom(parameterTypes[i])) {
                                result = fluentValidator
                                        .on(arguments[i], addOnValidatorChain)
                                        .onEach((Collection) arguments[i],
                                                new HibernateSupportedValidator()
                                                        .setHibernateDefaultErrorCode(hibernateDefaultErrorCode)
                                                        .setHiberanteValidator(validator))
                                        .when(arguments[i] != null)
                                        .onEach((Collection) arguments[i])
                                        .doValidate(callback)
                                        .result(toComplex());
                            } else if (parameterTypes[i].isArray()) {
                                result = fluentValidator
                                        .on(arguments[i], addOnValidatorChain)
                                        .onEach(ArrayUtil.toWrapperIfPrimitive(arguments[i]),
                                                new HibernateSupportedValidator()
                                                        .setHibernateDefaultErrorCode(hibernateDefaultErrorCode)
                                                        .setHiberanteValidator(validator))
                                        .when(arguments[i] != null)
                                        .onEach(ArrayUtil.toWrapperIfPrimitive(arguments[i]))
                                        .doValidate(callback)
                                        .result(toComplex());
                            } else {
                                result = fluentValidator
                                        .on(arguments[i], addOnValidatorChain)
                                        .on(arguments[i],
                                                new HibernateSupportedValidator()
                                                        .setHibernateDefaultErrorCode(hibernateDefaultErrorCode)
                                                        .setHiberanteValidator(validator))
                                        .when(arguments[i] != null)
                                        .on(arguments[i])
                                        .doValidate(callback)
                                        .result(toComplex());
                            }

                            if (result != null) {
                                LOGGER.debug(result.toString());
                            }

                            continue start;
                        }
                    }
                }
            }

            return invocation.proceed();
        } catch (RuntimeValidateException e) {
            throw e.getCause();
        } catch (Throwable throwable) {
            throw throwable;
        }
    }

    /**
     * 将需要额外验证的验证器封装为{@link ValidatorChain}
     *
     * @param fluentValid 参数上的注解装饰
     *
     * @return 验证器链
     */
    private ValidatorChain getAddOnValidatorChain(FluentValid fluentValid) {
        ValidatorChain chain = new ValidatorChain();
        chain.setValidators(getAddOnValidators(fluentValid));
        return chain;
    }

    /**
     * 将参数注解上的{@link FluentValid}内的<code>value</code>设置的{@link com.baidu.unbiz.fluentvalidator.Validator}在上下文中查询，返回列表
     *
     * @param fluentValid 参数上的注解装饰
     *
     * @return 验证器列表
     */
    private List<com.baidu.unbiz.fluentvalidator.Validator> getAddOnValidators(FluentValid fluentValid) {
        Class<? extends com.baidu.unbiz.fluentvalidator.Validator>[] addOnValidatorClasses;
        List<com.baidu.unbiz.fluentvalidator.Validator> addOnValidators = null;
        if (!ArrayUtil.isEmpty(fluentValid.value())) {
            LOGGER.debug(String.format("{} additional validators found"), fluentValid.value().length);
            addOnValidatorClasses = fluentValid.value();
            addOnValidators = CollectionUtil.createArrayList(fluentValid.value().length);
            for (Class<? extends com.baidu.unbiz.fluentvalidator.Validator> addOnValidatorClass :
                    addOnValidatorClasses) {
                List<?> beans = registry.findByType(addOnValidatorClass);
                if (!CollectionUtil.isEmpty(beans)) {
                    addOnValidators.add((com.baidu.unbiz.fluentvalidator.Validator) beans.get(0));
                }
            }
        }
        return addOnValidators;
    }

    public void setCallback(ValidateCallback callback) {
        this.callback = callback;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setHibernateDefaultErrorCode(int hibernateDefaultErrorCode) {
        this.hibernateDefaultErrorCode = hibernateDefaultErrorCode;
    }

    public void setFluentValidatorPostProcessor(
            FluentValidatorPostProcessor fluentValidatorPostProcessor) {
        this.fluentValidatorPostProcessor = fluentValidatorPostProcessor;
    }
}
