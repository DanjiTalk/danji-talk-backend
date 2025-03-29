package com.danjitalk.danjitalk.domain.chat.entity;

import com.danjitalk.danjitalk.domain.chat.enums.ChatRequestStatus;
import com.danjitalk.danjitalk.domain.common.entity.BaseEntity;
import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRequest extends BaseEntity { // 1대1 채팅 요청

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    @NotNull
    private Member requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    @NotNull
    private Member receiver;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ChatRequestStatus status;

    private Long chatroomId;

    @Builder
    public ChatRequest(Member requester, Member receiver, ChatRequestStatus status, Long chatroomId) {
        this.requester = requester;
        this.receiver = receiver;
        this.status = status;
        this.chatroomId = chatroomId;
    }

    public void changeStatus(ChatRequestStatus status) {
        this.status = status;
    }

     public void initRoom(Long chatroomId) {
        this.chatroomId = chatroomId;
    }
}
