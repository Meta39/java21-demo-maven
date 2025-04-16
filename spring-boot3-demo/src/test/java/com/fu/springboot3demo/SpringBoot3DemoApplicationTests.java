package com.fu.springboot3demo;

import com.fu.springboot3demo.util.ApplicationContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

@Slf4j
@SpringBootTest
public class SpringBoot3DemoApplicationTests {

    @Test
    void test() {

    }

    /**
     * 无参根据bean name执行相应的方法
     */
    @Test
    public void testNoParam() {
        Object bean = ApplicationContextUtils.getBean("invokeMethodByBeanNameService");
        Method method = ReflectionUtils.findMethod(bean.getClass(), "noParamsMethod");
        Object result = ReflectionUtils.invokeMethod(method, bean);
        log.info("result:{}", result);
    }

    /**
     * 以前的省事懒鬼做法，就是统一post请求地址，如： localhost:8080/serviceImpl?method=method。
     * 1、通过request.getUri()获取serviceImpl 的 bean name，再通过request.getParameter("method");获取方法名。
     * 2、通过ReflectionUtils.findMethod(getClass, methodName);获取ServiceImpl类实例。
     * 3、通过ReflectionUtils.invokeMethod()执行方法获取返回内容。
     */
    @Test
    public void testHasParam() {
        Object bean = ApplicationContextUtils.getBean("invokeMethodByBeanNameService");
        //因为是json字符串所以这里是String.class
        Method method = ReflectionUtils.findMethod(bean.getClass(), "haveParamsMethod", String.class);
        //"参数"其实就是json字符串。拿到以后再通过Jackson ObjectMapper 转成对应的
        Object result = ReflectionUtils.invokeMethod(method, bean, "{\"param\": \"http request json Body\"}");
        log.info("result:{}", result);
    }

}
