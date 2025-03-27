package com.danjitalk.danjitalk.infrastructure.repository.chat;

import com.danjitalk.danjitalk.domain.chat.entity.ChatRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRequestRepository extends JpaRepository<ChatRequest, Long> {

}
