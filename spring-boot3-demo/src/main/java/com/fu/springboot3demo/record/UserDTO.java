package com.fu.springboot3demo.record;

import java.io.Serializable;

/**
 * Java 14 新增记录，作为不可变的数据的载体。
 * 描述：与枚举一样，与类相比，记录也有一些限制，因此不会取代所有数据载体类。具体来说，它们不能替代可变的 JavaBean 类
 * 应用场景：记录可以在各种情况下用于对常见用例进行建模，如多个返回、流连接、复合键、树节点、DTO等，并提供更强的语义保证，使开发人员和框架能够更可靠地推断其状态。
 * 1、自动生成全参构造函数、无get前缀获取变量值方法。
 * 2、不需要 get/set 方法，直接通过.变量名()获取变量。
 * 3、会自动重写 equasl 、 hashCode 、 toString 方法，减少模板代码
 * 创建日期：2024-06-26
 */
public record UserDTO(Long id, String name) implements Serializable {
    //规范构造函数声明的紧凑版本，允许省略样板赋值。(类似全参构造函数)
    public UserDTO {
        if (id == null || id == 0L) {
            throw new IllegalArgumentException("id cannot be null or 0");
        }
    }

    //自定义构造方法
    public UserDTO(Long id) {
        this(id, null);
    }

    //静态方法
    public static String nameLikeRight(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        return name + "%";
    }

}
