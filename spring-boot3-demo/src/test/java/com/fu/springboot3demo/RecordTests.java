package com.fu.springboot3demo;

import com.fu.springboot3demo.record.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 创建日期：2024-06-26
 */
@Slf4j
public class RecordTests {

    @Test
    public void test() {
        UserDTO userDTO1 = new UserDTO(1L, "name");
        UserDTO userDTO2 = new UserDTO(1L, "name");
        log.info("record 会自动重写 equasl 和 hashCode 方法{}", userDTO1.equals(userDTO2));
        log.info("record 会自动重写 toString 方法{}", userDTO1);
        log.info("record 没有 get 和 set方法，因为record通过构造函数传参，是不可变的：{}", userDTO1.id());
    }

}
