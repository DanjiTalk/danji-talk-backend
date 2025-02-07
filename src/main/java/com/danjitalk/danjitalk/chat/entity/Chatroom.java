package com.danjitalk.danjitalk.chat.entity;

import com.danjitalk.danjitalk.common.util.CommonUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatroom {

    @Id
    @Column(length = 32, nullable = false, unique = true)
    private String id;

    private String name;

    private Boolean isPrivate;

    @OneToMany(mappedBy = "chatroom")
    private List<ChatroomMemberMapping> chatroomMemberList = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if(this.id == null) this.id = CommonUtil.generatedUUID();
    }
}
