package com.fu.springboot3starterdemo;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
//自动配置核心注解，需要配到resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports文件里面
@ComponentScan(basePackages = "com.fu.springboot3starterdemo.auto")//自动配置核心注解，当前项目哪些类需要自动配置
@ConditionalOnWebApplication//非自动配置核心注解，web应用才自动配置
public class SpringBoot3StarterAutoConfiguration {

}