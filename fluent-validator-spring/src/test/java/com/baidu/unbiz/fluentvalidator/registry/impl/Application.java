package com.baidu.unbiz.fluentvalidator.registry.impl;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangxu
 */
@SpringBootApplication
@ComponentScan(value = "com.baidu.unbiz")
@EnableAutoConfiguration
public class Application {

    @Bean
    SpringApplicationContextRegistry springApplicationContextRegistry(ApplicationContext applicationContext) {
        SpringApplicationContextRegistry s = new SpringApplicationContextRegistry();
        s.setApplicationContext(applicationContext);
        return s;
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
