package com.danjitalk.danjitalk.infrastructure.repository.chat;

import com.danjitalk.danjitalk.domain.chat.entity.ChatRequest;
import java.util.List;
import java.util.Optional;

public interface ChatRequestCustomRepository {

    Optional<ChatRequest> findChatRequestWithRequesterAndReceiverById(Long id);
    List<ChatRequest> findChatRequestWithRequesterByReceiverId(Long receiverId);
    List<ChatRequest> findChatRequestWithRequesterByRequesterId(Long requesterId);

}
