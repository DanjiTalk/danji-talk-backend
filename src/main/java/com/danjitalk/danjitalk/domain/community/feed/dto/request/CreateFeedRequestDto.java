package com.danjitalk.danjitalk.domain.community.feed.dto.request;

import com.danjitalk.danjitalk.domain.community.feed.enums.FeedType;

public record CreateFeedRequestDto(
        String title,
        String contents,
        FeedType feedType,
        Long apartmentId
) {
}
