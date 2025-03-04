package com.danjitalk.danjitalk.domain.community.feed.dto.request;

import java.time.LocalDateTime;

public record GetFeedListRequestDto(
        Long apartmentId,
        LocalDateTime cursorDate
) {
}
