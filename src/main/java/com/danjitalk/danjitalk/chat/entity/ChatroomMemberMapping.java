package com.danjitalk.danjitalk.chat.entity;

import com.danjitalk.danjitalk.common.util.CommonUtil;
import com.danjitalk.danjitalk.user.user.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomMemberMapping {

    @Id
    @Column(length = 32, nullable = false, unique = true)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Chatroom chatroom;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @PrePersist
    public void prePersist() {
        if(this.id == null) this.id = CommonUtil.generatedUUID();
    }

}
