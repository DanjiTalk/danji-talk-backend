package com.danjitalk.danjitalk.domain.community.feed.dto.response;

import com.danjitalk.danjitalk.domain.community.feed.enums.FeedType;

public record CreateFeedResponseDto(
        Long id,
        String title,
        String contents,
        FeedType feedType,
        String fileUrl
) {
}
