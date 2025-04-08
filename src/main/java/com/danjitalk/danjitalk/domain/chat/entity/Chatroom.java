package com.danjitalk.danjitalk.domain.chat.entity;

import com.danjitalk.danjitalk.domain.chat.enums.ChatroomType;
import com.danjitalk.danjitalk.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatroom extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Boolean isPrivate;

    @Enumerated(EnumType.STRING)
    private ChatroomType type;

    @OneToMany(mappedBy = "chatroom")
    private List<ChatroomMemberMapping> chatroomMemberList = new ArrayList<>();

    @Builder
    public Chatroom(String name, Boolean isPrivate, ChatroomType type) {
        this.name = name;
        this.isPrivate = isPrivate;
        this.type = type;
    }
}
