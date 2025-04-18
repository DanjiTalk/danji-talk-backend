package com.danjitalk.danjitalk.common.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException {

    public ConflictException() {
        super(HttpStatus.CONFLICT.value(), "이미 존재하는 데이터입니다. 다른 데이터를 사용해주세요.");
    }

    public ConflictException(String message) {
        super(HttpStatus.CONFLICT.value(), message);
    }
}