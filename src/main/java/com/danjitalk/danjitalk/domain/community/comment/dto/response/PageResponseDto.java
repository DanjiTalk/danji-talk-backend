package com.danjitalk.danjitalk.domain.community.comment.dto.response;

import java.util.List;

public record PageResponseDto<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
