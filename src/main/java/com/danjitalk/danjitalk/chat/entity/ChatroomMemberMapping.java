package com.danjitalk.danjitalk.chat.entity;

import com.danjitalk.danjitalk.user.user.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
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

}
