package com.danjitalk.danjitalk.domain.community.bookmark.dto.request;

import com.danjitalk.danjitalk.domain.community.bookmark.entity.Bookmark;
import jakarta.validation.constraints.NotBlank;

public record BookmarkRequestDto(
        @NotBlank(message = "게시글 ID는 필수값입니다.") Long feedId
) {

    /**
     * entity 변환
     * */
    public Bookmark toEntity(Long memberId) {
        return new Bookmark(null, this.feedId, memberId);
    }
}
