package com.danjitalk.danjitalk.domain.chat.entity;

import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomMemberMapping {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Chatroom chatroom;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String chatroomName;    // 개인 별 채팅방 이름 지정 1대1은 상대방 이름으로, 단체는 방장이 정해준 것 default

    @Builder
    public ChatroomMemberMapping(Chatroom chatroom, Member member, String chatroomName) {
        this.chatroom = chatroom;
        this.member = member;
        this.chatroomName = chatroomName;
        chatroom.getChatroomMemberList().add(this);
    }
}
