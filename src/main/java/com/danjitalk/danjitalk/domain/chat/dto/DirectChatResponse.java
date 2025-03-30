package com.danjitalk.danjitalk.domain.chat.dto;

import java.time.LocalDateTime;

public record DirectChatResponse(
    Long chatroomId,
    MemberInformation memberInformation,
    String chatMessage,
    LocalDateTime messageCreatedAt
) {

}
