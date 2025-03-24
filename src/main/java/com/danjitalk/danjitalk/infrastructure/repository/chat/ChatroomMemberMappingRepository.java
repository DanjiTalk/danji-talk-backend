package com.danjitalk.danjitalk.infrastructure.repository.chat;

import com.danjitalk.danjitalk.domain.chat.entity.ChatroomMemberMapping;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomMemberMappingRepository extends JpaRepository<ChatroomMemberMapping, Long> {

    List<ChatroomMemberMapping> findChatroomMemberMappingByMemberId(Long memberId);
}
