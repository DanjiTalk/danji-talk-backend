package com.danjitalk.danjitalk.domain.chat.dto;

import com.danjitalk.danjitalk.domain.chat.enums.ChatRequestStatus;
import java.time.LocalDateTime;

public record ChatRequestResponse(
    String message,
    MemberInformation memberInformation,
    Long requestId,
    ChatRequestStatus status,
    LocalDateTime createdAt
) {

}
