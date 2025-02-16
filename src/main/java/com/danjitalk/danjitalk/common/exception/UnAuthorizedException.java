package com.danjitalk.danjitalk.common.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends BaseException{

    public UnAuthorizedException() {
        super(HttpStatus.UNAUTHORIZED.value(), "인증되지 않은 요청입니다.");
    }
}
