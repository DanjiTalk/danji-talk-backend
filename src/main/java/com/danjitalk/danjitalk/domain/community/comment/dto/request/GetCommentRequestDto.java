package com.danjitalk.danjitalk.domain.community.comment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GetCommentRequestDto(

        @NotBlank(message = "페이지 번호는 필수입니다. 첫 페이지는 0번입니다.")
        int page,       // page = 0부터 시작

        @NotBlank(message = "페이지 사이즈는 필수입니다.")
        int size
) {
}
