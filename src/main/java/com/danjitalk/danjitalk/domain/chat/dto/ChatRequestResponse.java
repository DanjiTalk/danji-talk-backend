package com.danjitalk.danjitalk.domain.chat.dto;

import com.danjitalk.danjitalk.domain.chat.enums.ChatRequestStatus;

public record ChatRequestResponse(
    String message,
    MemberInformation memberInformation,
    Long requestId,
    ChatRequestStatus status
) {

}
