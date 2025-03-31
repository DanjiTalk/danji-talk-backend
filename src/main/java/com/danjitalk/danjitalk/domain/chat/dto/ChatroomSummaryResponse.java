package com.danjitalk.danjitalk.domain.chat.dto;

import java.time.LocalDateTime;

public record ChatroomSummaryResponse(
    Long chatroomId,
    MemberInformation memberInformation,
    String chatMessage,
    LocalDateTime messageCreatedAt
) {

}
