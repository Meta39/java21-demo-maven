package com.fu.springboot3starterdemo.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Res<T> {
    private Integer code;
    private String message;
    private T data;

    /**
     * 无返回值的成功
     */
    public static <T> Res<T> ok() {
        return ok(null);
    }

    /**
     * 有返回值的成功
     * @param data 响应数据
     */
    public static <T> Res<T> ok(T data) {
        return new Res<>(0, "success", data);
    }

    /**
     * 普通异常，默认状态码
     * @param msg 自定义错误信息
     */
    public static Res<?> error(String msg) {
        return error(1, msg);
    }

    /**
     * 需要状态码的异常，一般会用枚举值存放状态码和错误信息。如：未认证、未授权等。
     * @param code 状态码
     * @param msg 异常信息
     */
    public static Res<?> error(Integer code, String msg) {
        return new Res<>(code, msg, null);
    }

}
