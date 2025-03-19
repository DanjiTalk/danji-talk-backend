package com.danjitalk.danjitalk.domain.chat.entity;

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
public class Chatroom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Boolean isPrivate;

    @OneToMany(mappedBy = "chatroom")
    private List<ChatroomMemberMapping> chatroomMemberList = new ArrayList<>();

    @Builder
    public Chatroom(String name, Boolean isPrivate) {
        this.name = name;
        this.isPrivate = isPrivate;
    }
}
