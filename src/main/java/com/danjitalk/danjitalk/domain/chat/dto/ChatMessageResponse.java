package com.danjitalk.danjitalk.domain.chat.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ChatMessageResponse(
    String id,
    Long chatroomId,
    MemberInformation sender,
    String message,
    String imageUrl,
    LocalDateTime createdAt
) {

}
