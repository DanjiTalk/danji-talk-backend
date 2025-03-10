package com.danjitalk.danjitalk.domain.community.comment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequestDto(

        Long parentId,

        @NotBlank(message = "게시글 ID도 필수입니다.")
        Long feedId,

        @NotBlank(message = "댓글 내용은 필수입니다.")
        String contents

) {
}
