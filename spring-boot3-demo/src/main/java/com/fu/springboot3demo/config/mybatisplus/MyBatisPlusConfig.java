package com.fu.springboot3demo.config.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//TODO 如果使用这个方式的话,记得一定要指定 sqlSessionTemplateRef 或 sqlSessionFactoryRef https://github.com/mybatis/spring-boot-starter/wiki/Quick-Start-for-building-native-image#how-to-use-mapperscan
//@MapperScan(basePackages = "com.fu.springboot3demo", sqlSessionTemplateRef = "sqlSessionTemplate")
public class MyBatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        //添加分页插件。addInnerInterceptor设置数据库类型
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return mybatisPlusInterceptor;
    }

}

