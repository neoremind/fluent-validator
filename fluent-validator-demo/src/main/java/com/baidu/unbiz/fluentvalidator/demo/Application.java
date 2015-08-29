package com.baidu.unbiz.fluentvalidator.demo;

import java.util.Arrays;
import java.util.Locale;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.baidu.unbiz.fluentvalidator.exception.RuntimeValidateException;
import com.baidu.unbiz.fluentvalidator.registry.impl.SpringApplicationContextRegistry;

/**
 * @author zhangxu
 */
@SpringBootApplication
@ComponentScan(value = "com.baidu.unbiz")
@EnableAutoConfiguration
@Configuration
@Aspect
public class Application {

    @Bean
    SpringApplicationContextRegistry springApplicationContextRegistry(ApplicationContext applicationContext) {
        SpringApplicationContextRegistry s = new SpringApplicationContextRegistry();
        s.setApplicationContext(applicationContext);
        return s;
    }

    @Around("execution(* com.baidu.unbiz.fluentvalidator.demo.service.*.*(..))")
    public Object validateInterceptor(ProceedingJoinPoint pjp) throws Throwable {
        try {
            return pjp.proceed();
        } catch (RuntimeValidateException e) {
            throw e.getCause();
        }
    }

    @Bean
    public Validator hibernateValidator() {
        Locale.setDefault(Locale.ENGLISH);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }

}
