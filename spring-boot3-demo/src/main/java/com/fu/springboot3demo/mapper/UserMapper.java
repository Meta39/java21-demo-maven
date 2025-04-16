package com.fu.springboot3demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fu.springboot3demo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper //尽量显式的使用@Mapper，而不是使用
public interface UserMapper extends BaseMapper<User> {

}
