package com.danjitalk.danjitalk.domain.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateChatRequest(
    Long receiverId,

    @NotBlank(message = "내용이 비어 있습니다.")
    @Size(max = 500, message = "메시지는 최대 500자까지 전송할 수 있습니다.")
    String message
) {

}
