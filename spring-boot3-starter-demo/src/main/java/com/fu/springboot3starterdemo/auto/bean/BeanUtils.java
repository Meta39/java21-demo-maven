package com.fu.springboot3starterdemo.auto.bean;

import com.fu.springboot3starterdemo.api.PostApi;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Component
public final class BeanUtils implements ApplicationContextAware, ApplicationRunner {
    private BeanUtils() {
    }

    private static ApplicationContext context;
    public static final Map<String, Class<?>> BEAN_TYPE_MAP = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, PostApi> beans = context.getBeansOfType(PostApi.class);
        //循环map，forEach(key,value) 是最现代的方式，使用起来简洁明了。也可以用 for (Map.Entry<String, IWebService> entry : beans.entrySet()){}。
        beans.forEach((bean, type) -> {
            // AopProxyUtils.ultimateTargetClass 解决Spring Boot 使用 @Transactional 事务注解的问题。
            Class<?> beanClass = AopProxyUtils.ultimateTargetClass(type);
            // 获取 IWebService 实现类的泛型类型
            Type[] genericInterfaces = beanClass.getGenericInterfaces();
            for (Type genericInterface : genericInterfaces) {
                if (genericInterface instanceof ParameterizedType parameterizedType) {
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    if (actualTypeArguments.length > 0) {
                        Class<?> parameterType = (Class<?>) actualTypeArguments[0];
                        //把泛型入参放入缓存。防止每次请求都通过反射获取入参，影响程序性能。
                        BEAN_TYPE_MAP.put(bean, parameterType);
                    }
                }
            }
        });
    }

    //获取bean
    public static Object getBean(String name) throws BeansException {
        return context.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return context.getBean(name, requiredType);
    }

    public static Object getBean(String name, Object... args) throws BeansException {
        return context.getBean(name, args);
    }

    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return context.getBean(requiredType);
    }

    public static <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
        return context.getBean(requiredType, args);
    }
}
