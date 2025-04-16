package com.fu.springboot3demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching //启用缓存并使用 caffeine
//@EnableScheduling //启用定时任务【需要自定义 ThreadPoolTaskScheduler，否则是 @Scheduled 单线程的，即：ThreadPoolTaskScheduler.poolSize = 1】
//TODO 如果使用这个方式的话,记得一定要指定 sqlSessionTemplateRef 或 sqlSessionFactoryRef https://github.com/mybatis/spring-boot-starter/wiki/Quick-Start-for-building-native-image#how-to-use-mapperscan
//@MapperScan(basePackages = "com.fu.springboot3demo", sqlSessionTemplateRef = "sqlSessionTemplate")
@SpringBootApplication(proxyBeanMethods = false)
public class SpringBoot3DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot3DemoApplication.class, args);
    }

}