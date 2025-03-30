package com.danjitalk.danjitalk.infrastructure.mongo.chat;

import com.danjitalk.danjitalk.domain.chat.entity.ChatMessage;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageMongoRepository extends MongoRepository<ChatMessage, String> {

    Optional<ChatMessage> findTopByChatroomIdOrderByCreatedAtDesc(Long chatroomId);
}
