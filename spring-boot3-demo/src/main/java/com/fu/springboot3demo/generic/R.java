package com.fu.springboot3demo.generic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一返回类
 * 创建日期：2024-08-09
 */
@Data
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class R<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> R<T> ok() {
        return ok(null);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(1, "success", data);
    }

    public static <T> R<T> err(String message) {
        return err(0, message);
    }

    public static <T> R<T> err(Integer code, String message) {
        return new R<>(code, message, null);
    }

}