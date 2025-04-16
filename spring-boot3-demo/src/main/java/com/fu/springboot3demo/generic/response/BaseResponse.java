package com.fu.springboot3demo.generic.response;

import lombok.*;

/**
 * 比较脑残的统一返回类，把状态码、信息放到子类同级。
 * 如下所示（把状态码、信息放到和子类同级）：
 * {"code":1,"message":"success","id":1}
 * {"code":1,"message":"success","username":"root"}
 * 正常情况如下所示（状态码、信息和返回的数据分离）：
 * {"code":1,"message":"success","response1":{"id":1}}
 * {"code":1,"message":"success","response2":{"username":"root"}}
 * 优化：
 * 1、设置为抽象类，不让实例化BaseResponse，只能实例化子类。
 * 2、受保护的set方法，不让子类setCode，防止子类乱设置code值和message。
 * @since 2024-08-14
 */
@Getter
@ToString
@EqualsAndHashCode
public abstract class BaseResponse<T extends BaseResponse<T>> {
    private Integer code;
    private String message;

    protected void setCode(Integer code) {
        this.code = code;
    }

    protected void setMessage(String message) {
        this.message = message;
    }

    @SuppressWarnings("unchecked")
    public final T ok() {
        this.setCode(1);
        this.setMessage("success");
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public final T err(String message) {
        this.setCode(0);
        this.setMessage(message);
        return (T) this;
    }

}