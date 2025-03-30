package com.danjitalk.danjitalk.infrastructure.repository.chat;

import com.danjitalk.danjitalk.domain.chat.entity.ChatRequest;
import com.danjitalk.danjitalk.domain.chat.enums.ChatRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRequestRepository extends JpaRepository<ChatRequest, Long>, ChatRequestCustomRepository {

    boolean existsByRequesterIdAndReceiverIdAndStatus(Long requesterId, Long receiverId, ChatRequestStatus status);
}
