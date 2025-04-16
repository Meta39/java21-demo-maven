package com.fu.springboot3demo.controller;

import com.fu.springboot3demo.entity.Swagger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Swagger
 */
@Tag(name = "Controller 的简单描述(XXX接口)",description = "Controller详细描述（XXX接口CRUD）")
@RestController
@RequestMapping("swagger")
public class SwaggerController {

    @Operation(summary = "请求方法简单描述(查询)" , description = "请求方法详细描述(通过ID查询XXX，ID必传)")
    @GetMapping("read/{param}")
    public String read(@PathVariable @Parameter(description = "请求参数描述（param参数必传）") String param){
        return "read" + param;
    }

    @GetMapping("schema")
    public Swagger schema(){
        Swagger swagger = new Swagger();
        swagger.setSwagger("丝袜哥");
        return swagger;
    }
}
