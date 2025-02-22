package com.danjitalk.danjitalk.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {

    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST.value(), "요청 값이 잘못되었습니다.");
    }

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
