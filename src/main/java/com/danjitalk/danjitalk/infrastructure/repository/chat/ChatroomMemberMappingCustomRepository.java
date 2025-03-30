package com.danjitalk.danjitalk.infrastructure.repository.chat;

import com.danjitalk.danjitalk.domain.chat.entity.ChatroomMemberMapping;
import java.util.List;

public interface ChatroomMemberMappingCustomRepository {

    List<ChatroomMemberMapping> findChatroomMemberMappingWithMemberAndChatroomByMemberId(Long memberId);
}
