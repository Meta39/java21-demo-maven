package com.fu.springboot3starterdemo.exception;

import java.io.Serial;

/**
 * 自定义运行时异常
 * 继承这个类，可以实现抛出自定义code的异常和错误信息。
 */
public abstract class PostApiException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    protected Integer code;

    public PostApiException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public PostApiException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
