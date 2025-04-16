package com.fu.springboot3demo.controller;

import com.fu.springboot3demo.entity.User;
import com.fu.springboot3demo.service.InvokeMethodByBeanNameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {
    private final InvokeMethodByBeanNameService invokeMethodByBeanNameService;

    @PostMapping("user")
    public User user(@RequestBody User user){
        return user;
    }

    @GetMapping(value = "get")
    public String get(){
        return "hello";
    }

    @GetMapping("hello")
    public User hello(){
        User user = new User();
        user.setId(1L);
        user.setName("轩辕镜城");
        return user;
    }

    @GetMapping("invoke")
    public String invoke(){
        return invokeMethodByBeanNameService.noParamsMethod();
    }

}
