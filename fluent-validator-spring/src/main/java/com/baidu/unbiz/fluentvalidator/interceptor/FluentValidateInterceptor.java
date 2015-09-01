package com.baidu.unbiz.fluentvalidator.interceptor;

import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toComplex;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

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
import com.baidu.unbiz.fluentvalidator.DefaulValidateCallback;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ValidateCallback;
import com.baidu.unbiz.fluentvalidator.annotation.FluentValid;
import com.baidu.unbiz.fluentvalidator.exception.RuntimeValidateException;
import com.baidu.unbiz.fluentvalidator.jsr303.HibernateSupportedValidator;
import com.baidu.unbiz.fluentvalidator.registry.impl.SpringApplicationContextRegistry;
import com.baidu.unbiz.fluentvalidator.util.ArrayUtil;
import com.baidu.unbiz.fluentvalidator.util.Preconditions;
import com.baidu.unbiz.fluentvalidator.util.ReflectionUtil;

/**
 * 与Spring集成的拦截器，结合{@link FluentValid}注解装饰在参数前面，可以利用AOP来拦截请求，对参数进行校验
 *
 * @author zhangxu
 */
public class FluentValidateInterceptor implements MethodInterceptor, InitializingBean, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(FluentValidateInterceptor.class);

    private ApplicationContext applicationContext;

    private ValidateCallback callback = new DefaulValidateCallback();

    private SpringApplicationContextRegistry registry;

    /**
     * hibernate validator
     */
    private Validator validator;

    @Override
    public void afterPropertiesSet() throws Exception {
        registry = new SpringApplicationContextRegistry();
        registry.setApplicationContext(applicationContext);

        // 如果hibernate validator通过spring配置了，则不进行初始化
        if (validator == null) {
            // init hibernate validator
            // Locale.setDefault(Locale.ENGLISH);
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }
    }

    /**
     * 拦截方法进行验证，如果参数前面存在{@link FluentValid}注解，则进行校验，分一下三中情况：
     * <ul>
     * <li>1. 普通对象，直接验证</li>
     * <li>2. 列表对象，onEach验证</li>
     * <li>3. 数组对象，onEach验证</li>
     * </ul>
     * <p/>
     * 注：另外hibernate validator优先校验
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
                    for (int j = 0; j < paramAnnotation.length; i++) {
                        if (paramAnnotation[j].annotationType() == FluentValid.class) {
                            LOGGER.debug("Find @FluentValid annotation on index=[" + i + "] parameter and ready to "
                                    + "validate");

                            ComplexResult result;
                            if (Collection.class.isAssignableFrom(parameterTypes[i])) {
                                result = FluentValidator.checkAll().configure(registry)
                                        .onEach((Collection) arguments[i],
                                                new HibernateSupportedValidator().setHiberanteValidator(validator))
                                        .onEach((Collection) arguments[i])
                                        .doValidate(callback)
                                        .result(toComplex());
                            } else if (parameterTypes[i].isArray()) {
                                result = FluentValidator.checkAll().configure(registry)
                                        .onEach(ArrayUtil.toWrapperIfPrimitive(arguments[i]),
                                                new HibernateSupportedValidator().setHiberanteValidator(validator))

                                        .onEach(ArrayUtil.toWrapperIfPrimitive(arguments[i]))
                                        .doValidate(callback)
                                        .result(toComplex());
                            } else {
                                result = FluentValidator.checkAll().configure(registry)
                                        .on(arguments[i],
                                                new HibernateSupportedValidator().setHiberanteValidator(validator))
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
}
