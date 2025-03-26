package com.danjitalk.danjitalk.domain.chat.entity;

import com.danjitalk.danjitalk.domain.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseEntity {

    @Id
    private String id;
    private Long chatroomId;
    private Long senderId;
    private String message;
    private String imageUrl;

    @Builder
    public ChatMessage(Long chatroomId, Long senderId, String message, String imageUrl) {
        this.chatroomId = chatroomId;
        this.senderId = senderId;
        this.message = message;
        this.imageUrl = imageUrl;
    }
}
