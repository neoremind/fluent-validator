package com.baidu.unbiz.fluentvalidator;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.unbiz.fluentvalidator.annotation.FluentValid;
import com.baidu.unbiz.fluentvalidator.annotation.FluentValidate;
import com.baidu.unbiz.fluentvalidator.registry.Registry;
import com.baidu.unbiz.fluentvalidator.util.CollectionUtil;
import com.baidu.unbiz.fluentvalidator.util.ReflectionUtil;

/**
 * 通过注解验证方式验证，内部缓存待验证类和<code>AnnotationValidator</code>列表，以及注解中的<code>Validator</code>
 *
 * @author zhangxu
 */
public class AnnotationValidatorCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationValidatorCache.class);

    /**
     * Bean的所有待验证属性列表的缓存Map
     */
    private static ConcurrentHashMap<Class<?>, List<AnnotationValidator>> CLASS_2_ANNOTATION_VALIDATOR_MAP =
            new ConcurrentHashMap<Class<?>, List<AnnotationValidator>>();

    /**
     * 验证器对象缓存
     */
    private static ConcurrentHashMap<Class<? extends Validator>, Validator> VALIDATOR_MAP = new
            ConcurrentHashMap<Class<? extends Validator>, Validator>();

    /**
     * 获取Bean的所有待验证属性的验证列表
     *
     * @param registry 验证器对象寻找注册器
     * @param object   待验证对象
     *
     * @return 待验证属性的验证列表
     */
    public static List<AnnotationValidator> getAnnotationValidator(Registry registry, Object object) {
        if (registry == null || object == null) {
            return Collections.emptyList();
        }
        Class clazz = object.getClass();
        if (!CLASS_2_ANNOTATION_VALIDATOR_MAP.containsKey(clazz)) {
            addByClass(registry, object.getClass());
        }
        return CLASS_2_ANNOTATION_VALIDATOR_MAP.get(clazz);
    }

    /**
     * 为某个类添加所有待验证属性的<code>AnnotationValidator</code>到<code>CLASS_2_ANNOTATION_VALIDATOR_MAP</code>中
     * <p/>
     * 流程如下：
     * <ul>
     * <li>1. 如果<code>CLASS_2_ANNOTATION_VALIDATOR_MAP</code>已缓存了类对应的验证器，则直接退出</li>
     * <li>2. 寻找类中所有装饰有{@link FluentValidate}注解的属性，遍历之。</li>
     * <li>3. 取出{@link FluentValidate}注解中的值，包含所有验证器的类定义。</li>
     * <li>4. 遍历所有的验证器类定义，如果验证器类不是实现了<code>Validator</code>接口跳过。</li>
     * <li>5. <code>VALIDATOR_MAP</code>中如果已经存在了改验证器类对应的实例，则跳过，如果没有就尝试从<code>Registry</code
     * >中查询实例对象，一般情况下当前线程classLoader下只有一个实例，当使用Spring等IoC容器的时候会出现多个，则取第一个，放入 <code>validatorsMap</code>中。
     * </li>
     * <li>6. 组装<code>AnnotationValidator</code>，放到<code>CLASS_2_ANNOTATION_VALIDATOR_MAP</code>缓存住。</li>
     * </ul>
     *
     * @param registry 验证器对象寻找注册器
     * @param clazz    待验证类定义
     */
    private static void addByClass(Registry registry, Class<?> clazz) {
        try {
            if (CLASS_2_ANNOTATION_VALIDATOR_MAP.contains(clazz)) {
                return;
            }
            List<AnnotationValidator> annotationValidators = getAllAnnotationValidators(registry, clazz);

            if (CollectionUtil.isEmpty(annotationValidators)) {
                LOGGER.warn(String.format("Annotation-based validation enabled for %s, and to-do validators are empty",
                        clazz.getSimpleName()));
            } else {
                CLASS_2_ANNOTATION_VALIDATOR_MAP.putIfAbsent(clazz, annotationValidators);
                LOGGER.debug(
                        String.format("Annotation-based validation added for %s, and to-do validators are %s", clazz
                                .getSimpleName(), annotationValidators));
            }
        } catch (Exception e) {
            LOGGER.error("Failed to add annotation validators " + e.getMessage(), e);
        }
    }

    /**
     * 根据注册器以及类获取所有需要被验证的属性，封装为<code>AnnotationValidator</code>列表返回
     *
     * @param registry 验证器对象寻找注册器
     * @param clazz    待验证类定义
     *
     * @return <code>AnnotationValidator</code>列表
     */
    private static List<AnnotationValidator> getAllAnnotationValidators(Registry registry, Class<?> clazz) {
        List<AnnotationValidator> annotationValidators = CollectionUtil.createArrayList();

        Field[] fields = ReflectionUtil.getAnnotationFields(clazz, FluentValidate.class);
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            FluentValidate fluentValidateAnnt = ReflectionUtil.getAnnotation(field, FluentValidate.class);

            Class<? extends Validator>[] validatorClasses = fluentValidateAnnt.value();
            Class<?>[] groups = fluentValidateAnnt.groups();
            if (validatorClasses == null || validatorClasses.length == 0) {
                LOGGER.warn(String.format("No validator annotation bound %s#%s", clazz.getSimpleName(),
                        field.getName()));
                continue;
            }

            List<Validator> validators = CollectionUtil.createArrayList(4);
            for (Class<? extends Validator> validatorClass : validatorClasses) {
                if (!Validator.class.isAssignableFrom(validatorClass)) {
                    LOGGER.warn(String.format("Validator annotation class %s is not assignable from %s",
                            validatorClass.getSimpleName(), Validator.class.getSimpleName()));
                    continue;
                }
                if (!VALIDATOR_MAP.containsKey(validatorClass)) {
                    List<? extends Validator> validatorsFound = registry.findByType(validatorClass);
                    if (CollectionUtil.isEmpty(validatorsFound)) {
                        LOGGER.warn(String.format("Validator annotation class %s not found or init failed for "
                                + "%s#%s", validatorClass.getSimpleName(), clazz.getSimpleName(), field.getName()));
                        continue;
                    }
                    if (validatorsFound.size() > 1) {
                        LOGGER.warn(String.format(
                                "Validator annotation class %s found multiple instances for %s#%s, so the first "
                                        + "one will be used",
                                validatorClass.getSimpleName(), clazz.getSimpleName(), field.getName()));
                    }
                    VALIDATOR_MAP.putIfAbsent(validatorClass, validatorsFound.get(0));
                    validators.add(VALIDATOR_MAP.get(validatorClass));
                    LOGGER.info(String.format("Cached validator %s", validatorClass.getSimpleName()));
                }
            	validators.add(VALIDATOR_MAP.get(validatorClass));
            }

            if (CollectionUtil.isEmpty(validators)) {
                LOGGER.warn(String.format("Annotation-based validation enabled but none of the validators is "
                        + "applicable for %s#%s", clazz.getSimpleName(), field.getName()));
                continue;
            }

            AnnotationValidator av = new AnnotationValidator();
            av.setField(field);
            av.setMethod(ReflectionUtil.getGetterMethod(clazz, field));
            av.setValidators(validators);
            av.setGroups(groups);
            annotationValidators.add(av);
            LOGGER.trace("Annotation-based validation added " + av);
        }

        fields = ReflectionUtil.getAnnotationFields(clazz, FluentValid.class);
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            FluentValid cascadeAnnt = ReflectionUtil.getAnnotation(field, FluentValid.class);

            AnnotationValidator av = new AnnotationValidator();
            av.setField(field);
            av.setMethod(ReflectionUtil.getGetterMethod(clazz, field));
            av.setValidators(null);
            av.setGroups(null);
            av.setIsCascade(cascadeAnnt != null);
            annotationValidators.add(av);
            LOGGER.trace("Cascade annotation-based validation added " + av);
        }

        return annotationValidators;
    }

}
