package com.danjitalk.danjitalk.common.exception;

import com.danjitalk.danjitalk.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException e) {
        return ResponseEntity
            .status(e.getCode())
            .body(ApiResponse.fail(e.getCode(), e.getMessage()));
    }

}
