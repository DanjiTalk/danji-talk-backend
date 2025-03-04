package com.danjitalk.danjitalk.domain.community.feed.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record FeedListDto(
        List<FeedDto> feedDtoList,
        LocalDateTime cursorDate
) {
}
