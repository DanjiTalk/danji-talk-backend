package com.danjitalk.danjitalk.common.exception;

import org.springframework.http.HttpStatus;

public class DataNotFoundException extends BaseException {

    public DataNotFoundException() {
        super(HttpStatus.NOT_FOUND.value(), "해당 데이터가 삭제되었거나 존재하지 않습니다.");
    }

    public DataNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND.value(), message);
    }
}
