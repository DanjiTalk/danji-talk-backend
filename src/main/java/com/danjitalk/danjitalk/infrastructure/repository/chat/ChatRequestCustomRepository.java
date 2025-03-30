package com.danjitalk.danjitalk.infrastructure.repository.chat;

import com.danjitalk.danjitalk.domain.chat.entity.ChatRequest;
import java.util.Optional;

public interface ChatRequestCustomRepository {

    Optional<ChatRequest> findChatRequestWithRequesterAndReceiverById(Long id);
}
