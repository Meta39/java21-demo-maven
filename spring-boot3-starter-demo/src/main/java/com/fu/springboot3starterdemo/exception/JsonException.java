package com.fu.springboot3starterdemo.exception;

import java.io.Serial;

public final class JsonException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public JsonException() {
        super();
    }

    public JsonException(String message) {
        super(message);
    }

    public JsonException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonException(Throwable cause) {
        super(cause);
    }

}
