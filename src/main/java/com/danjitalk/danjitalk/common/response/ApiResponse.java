package com.danjitalk.danjitalk.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) // null인 필드를 제외
public record ApiResponse<T>(
    int code,
    String message,
    T data
) {
    public static <T> ApiResponse<T> success(int code, String message, T data) {
        return new ApiResponse<>(code, message, data);
    }

    public static ApiResponse<Void> fail(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
