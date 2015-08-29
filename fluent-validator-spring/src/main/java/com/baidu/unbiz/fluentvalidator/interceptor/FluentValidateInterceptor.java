package com.baidu.unbiz.fluentvalidator.interceptor;

import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toComplex;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

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
import com.baidu.unbiz.fluentvalidator.registry.impl.SpringApplicationContextRegistry;
import com.baidu.unbiz.fluentvalidator.util.ArrayUtil;
import com.baidu.unbiz.fluentvalidator.util.Preconditions;
import com.baidu.unbiz.fluentvalidator.util.ReflectionUtil;

/**
 * @author zhangxu
 */
public class FluentValidateInterceptor implements MethodInterceptor, InitializingBean, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(FluentValidateInterceptor.class);

    private ApplicationContext applicationContext;

    private ValidateCallback callback = new DefaulValidateCallback();

    private SpringApplicationContextRegistry registry;

    @Override
    public void afterPropertiesSet() throws Exception {
        registry = new SpringApplicationContextRegistry();
        registry.setApplicationContext(applicationContext);
    }

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

                            ComplexResult result = null;
                            if (Collection.class.isAssignableFrom(parameterTypes[i])) {
                                result = FluentValidator.checkAll().configure(registry)
                                        .onEach((Collection) arguments[i])
                                        .doValidate(callback)
                                        .result(toComplex());
                            } else if (parameterTypes[i].isArray()) {
                                result = FluentValidator.checkAll().configure(registry)
                                        .onEach(ArrayUtil.toWrapperIfPrimitive(arguments[i]))
                                        .doValidate(callback)
                                        .result(toComplex());
                            } else {
                                result = FluentValidator.checkAll().configure(registry)
                                        .on(arguments[i])
                                        .doValidate(callback)
                                        .result(toComplex());
                            }

                            LOGGER.debug(result.toString());
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

}
