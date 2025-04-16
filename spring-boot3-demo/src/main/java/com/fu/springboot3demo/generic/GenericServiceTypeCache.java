package com.fu.springboot3demo.generic;

import com.fu.springboot3demo.util.ApplicationContextUtils;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @since 2024-08-09
 */
@Component
public class GenericServiceTypeCache implements ApplicationRunner {
    /**
     * 只能在启动的时候 put，运行的时候 get。不能在运行的时候 put，因为 HashMap 不是线程安全的。
     */
    private static final Map<String, Class<?>> typeCache = new HashMap<>();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, GenericService> beans = ApplicationContextUtils.getBeansOfType(GenericService.class);
        //循环map，forEach(key,value) 是最现代的方式，使用起来简洁明了。也可以用 for (Map.Entry<String, IWebService> entry : beans.entrySet()){}。
        beans.forEach((bean, type) -> {
            // AopProxyUtils.ultimateTargetClass 解决Spring Boot 使用 @Transactional 事务注解的问题。
            Class<?> beanClass = AopProxyUtils.ultimateTargetClass(type);
            // 获取 GenericService 实现类的泛型类型
            Type[] genericInterfaces = beanClass.getGenericInterfaces();
            for (Type genericInterface : genericInterfaces) {
                if (genericInterface instanceof ParameterizedType parameterizedType) {
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    if (actualTypeArguments.length > 0) {
                        Class<?> parameterType = (Class<?>) actualTypeArguments[0];
                        //把泛型入参放入缓存。防止每次请求都通过反射获取入参，影响程序性能。
                        typeCache.put(bean, parameterType);
                    }
                }
            }
        });
    }

    /**
     * 通过缓存获取 GenericService 实现类 invoke 函数的 泛型入参
     *
     * @param serviceName GenericService 实现类的 bean name
     */
    public static Class<?> getRequestType(String serviceName) {
        return typeCache.get(serviceName);
    }

}
