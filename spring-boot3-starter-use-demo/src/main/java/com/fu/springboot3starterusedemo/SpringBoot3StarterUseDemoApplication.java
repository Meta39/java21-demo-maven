package com.fu.springboot3starterusedemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class SpringBoot3StarterUseDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SpringBoot3StarterUseDemoApplication.class, args);
        String[] beanNames = run.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            log.info(beanName);
        }
    }

}
