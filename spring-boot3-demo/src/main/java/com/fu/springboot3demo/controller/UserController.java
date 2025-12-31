package com.fu.springboot3demo.controller;

import com.fu.springboot3demo.entity.User;
import com.fu.springboot3demo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserMapper userMapper;

    @GetMapping
    public User getUsers() {
        return userMapper.selectById(2L);
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id) {
        return userMapper.findNameById(id);
    }

}
