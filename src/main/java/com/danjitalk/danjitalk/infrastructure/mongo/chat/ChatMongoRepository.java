package com.danjitalk.danjitalk.infrastructure.mongo.chat;

import com.danjitalk.danjitalk.domain.chat.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMongoRepository extends MongoRepository<ChatMessage, String> {

}
