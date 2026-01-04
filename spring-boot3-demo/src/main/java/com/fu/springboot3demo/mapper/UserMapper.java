package com.fu.springboot3demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fu.springboot3demo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper //尽量显式的使用@Mapper，而不是使用
public interface UserMapper extends BaseMapper<User> {

    /**
     * TODO 正常jar没问题，打成graalvm镜像以后执行就会报：Invalid bound statement (not found): com.fu.springboot3demo.mapper.UserMapper.findNameById
     */
    String findNameById(@Param("id") Long id);

}
