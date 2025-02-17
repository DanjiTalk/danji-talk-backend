package com.danjitalk.danjitalk.common.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final int code;

    public BaseException(int code, String message) {
        super(message);
        this.code = code;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
