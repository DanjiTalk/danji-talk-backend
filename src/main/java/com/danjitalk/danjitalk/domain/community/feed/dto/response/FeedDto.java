package com.danjitalk.danjitalk.domain.community.feed.dto.response;

import java.time.LocalDateTime;

public record FeedDto(
        Long feedId,
        Long memberId,
        String nickName,
        String title,
        String contents,
        LocalDateTime localDateTime,
        Integer reactionCount,
        Integer commentCount,
        String thumbnailFileUrl
) {
}
