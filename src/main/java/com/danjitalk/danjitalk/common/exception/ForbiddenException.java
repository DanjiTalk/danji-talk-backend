package com.danjitalk.danjitalk.common.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseException {

    public ForbiddenException() {
        super(HttpStatus.FORBIDDEN.value(), "권한이 없습니다.");
    }

    public ForbiddenException(int code, String message) {
        super(code, message);
    }
}
