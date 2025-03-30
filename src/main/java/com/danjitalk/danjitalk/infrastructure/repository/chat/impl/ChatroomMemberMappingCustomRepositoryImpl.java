package com.danjitalk.danjitalk.infrastructure.repository.chat.impl;

import com.danjitalk.danjitalk.domain.chat.entity.ChatroomMemberMapping;
import com.danjitalk.danjitalk.infrastructure.repository.chat.ChatroomMemberMappingCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

import static com.danjitalk.danjitalk.domain.chat.entity.QChatroomMemberMapping.chatroomMemberMapping;


@RequiredArgsConstructor
public class ChatroomMemberMappingCustomRepositoryImpl implements ChatroomMemberMappingCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChatroomMemberMapping> findChatroomMemberMappingWithMemberAndChatroomByMemberId(Long memberId) {
        return queryFactory.selectFrom(chatroomMemberMapping)
                .join(chatroomMemberMapping.member).fetchJoin()
                .join(chatroomMemberMapping.chatroom).fetchJoin()
                .where(chatroomMemberMapping.member.id.eq(memberId))
                .fetch();
    }
}
