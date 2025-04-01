package com.danjitalk.danjitalk.infrastructure.repository.chat;

import com.danjitalk.danjitalk.domain.chat.entity.ChatroomMemberMapping;
import com.danjitalk.danjitalk.domain.chat.enums.ChatroomType;
import java.util.List;

public interface ChatroomMemberMappingCustomRepository {

    List<ChatroomMemberMapping> findChatroomMemberMappingWithMemberAndChatroomByMemberId(Long memberId, ChatroomType chatroomType);
    List<ChatroomMemberMapping> findChatroomMemberMappingWithMemberByChatroomId(Long chatroomId);
}
