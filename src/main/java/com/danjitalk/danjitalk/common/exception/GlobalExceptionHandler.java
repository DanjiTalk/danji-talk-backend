package com.danjitalk.danjitalk.common.exception;

import com.danjitalk.danjitalk.common.response.ApiResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException e) {
        log.warn("BaseException: {}", e.getMessage());
        return ResponseEntity
            .status(e.getCode())
            .body(ApiResponse.fail(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(NoResourceFoundException.class) // 없는 주소 입력 시 404
    public ResponseEntity<ApiResponse<Void>> handleNoResourceFoundException(NoResourceFoundException e) {
        log.warn("handleNoResourceFoundException");
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.fail(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleRequestValidException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .toList();

        String errorMessage = String.join(" ", errors);

        log.warn("MethodArgumentNotValidException: {}", errorMessage);
        return ResponseEntity
            .badRequest()
            .body(ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("IllegalArgumentException: {}", e.getMessage());
        return ResponseEntity
            .badRequest()
            .body(ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Exception: {}", e.getMessage());
        return ResponseEntity
            .internalServerError()
            .body(ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

}
