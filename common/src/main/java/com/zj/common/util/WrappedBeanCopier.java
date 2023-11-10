package com.zj.common.util;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * 属性拷贝工具类
 */
public class WrappedBeanCopier {
    private static final Logger logger = LoggerFactory.getLogger(WrappedBeanCopier.class);
    /**
     * 复制器缓存
     */
    private static final Map<String, BeanCopier> BEAN_COPIER_CACHE = new ConcurrentHashMap<>();
    /**
     * 反射模板缓存
     */
    private static final Map<String, ConstructorAccess> CONSTRUCTOR_ACCESS_CACHE = new ConcurrentHashMap<>();

    /**
     * 复制属性
     *
     * @param source
     * @param target
     */
    private static void copyProperties(Object source, Object target) {
        BeanCopier copier = getBeanCopier(source.getClass(), target.getClass());
        copier.copy(source, target, null);
    }

    /**
     * 获取复制器
     *
     * @param sourceClass
     * @param targetClass
     * @return
     */
    private static BeanCopier getBeanCopier(Class sourceClass, Class targetClass) {
        String beanKey = generateKey(sourceClass, targetClass);
        BeanCopier copier = null;
        if (!BEAN_COPIER_CACHE.containsKey(beanKey)) {
            copier = BeanCopier.create(sourceClass, targetClass, false);
            BEAN_COPIER_CACHE.put(beanKey, copier);
        } else {
            copier = BEAN_COPIER_CACHE.get(beanKey);
        }
        return copier;
    }

    /**
     * 生成复制器缓存的key
     *
     * @param class1
     * @param class2
     * @return
     */
    private static String generateKey(Class<?> class1, Class<?> class2) {
        return class1.toString() + class2.toString();
    }

    /**
     * 复制类属性
     *
     * @param source
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        T t = null;
        try {
            t = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.debug("Create new instance of:{} failed: {}", targetClass, e.getMessage());
        }
        copyProperties(source, t);
        return t;
    }

    /**
     * 复制类属性-批量
     *
     * @param sourceList
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> Stream<T> copyPropertiesOfListToStream(List<?> sourceList, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return Stream.empty();
        }
        ConstructorAccess<T> constructorAccess = getConstructorAccess(targetClass);
        return sourceList.stream().map(o -> {
            try {
                T t = constructorAccess.newInstance();
                copyProperties(o, t);
                return t;
            } catch (Exception e) {
                logger.debug("CopyPropertiesOfListToStream RuntimeException{}", e);
                return null;
            }
        }).filter(Objects::nonNull);
    }

    /**
     * Java bean 转Map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> toMap(Object obj) {
        try {
            return PropertyUtils.describe(obj);
        } catch (Exception e) {
            logger.warn("Bean to map error", e);
            return new HashMap();
        }
    }

    /**
     * 复制类属性
     *
     * @param source
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T, S> T copyPropertiesWithEnd(S source, Class<T> targetClass, BiConsumer<T, S> consumer) {
        T t = null;
        try {
            t = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.debug("Create new instance of:{} failed: {}", targetClass, e.getMessage());
        }
        copyProperties(source, t);
        consumer.accept(t, source);
        return t;
    }

    /**
     * 复制类属性
     *
     * @param source
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> Optional<T> copyPropertiesOptional(Object source, Class<T> targetClass) {
        return Optional.ofNullable(copyProperties(source, targetClass));
    }

    /**
     * 复制类属性-批量
     *
     * @param sourceList
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> List<T> copyPropertiesOfList(List<?> sourceList, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return Collections.emptyList();
        }
        ConstructorAccess<T> constructorAccess = getConstructorAccess(targetClass);
        List<T> resultList = new ArrayList<>(sourceList.size());
        for (Object o : sourceList) {
            T t = null;
            try {
                t = constructorAccess.newInstance();
                copyProperties(o, t);
                resultList.add(t);
            } catch (Exception e) {
                logger.debug("CopyPropertiesOfList RuntimeException{}", e);
            }
        }
        return resultList;
    }

    /**
     * 获取反射模板
     *
     * @param targetClass
     * @param <T>
     * @return
     */
    private static <T> ConstructorAccess<T> getConstructorAccess(Class<T> targetClass) {
        ConstructorAccess<T> constructorAccess = CONSTRUCTOR_ACCESS_CACHE.get(targetClass.toString());
        if (constructorAccess != null) {
            return constructorAccess;
        }
        try {
            constructorAccess = ConstructorAccess.get(targetClass);
            constructorAccess.newInstance();
            CONSTRUCTOR_ACCESS_CACHE.put(targetClass.toString(), constructorAccess);
        } catch (Exception e) {
            logger.debug("Create new instance of:{} failed: {}", targetClass, e.getMessage());
        }
        return constructorAccess;
    }

}

