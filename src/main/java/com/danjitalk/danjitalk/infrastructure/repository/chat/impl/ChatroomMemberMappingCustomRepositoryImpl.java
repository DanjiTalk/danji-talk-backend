package com.danjitalk.danjitalk.infrastructure.repository.chat.impl;

import com.danjitalk.danjitalk.domain.chat.entity.ChatroomMemberMapping;
import com.danjitalk.danjitalk.domain.chat.enums.ChatroomType;
import com.danjitalk.danjitalk.infrastructure.repository.chat.ChatroomMemberMappingCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

import static com.danjitalk.danjitalk.domain.chat.entity.QChatroom.chatroom;
import static com.danjitalk.danjitalk.domain.chat.entity.QChatroomMemberMapping.chatroomMemberMapping;


@RequiredArgsConstructor
public class ChatroomMemberMappingCustomRepositoryImpl implements ChatroomMemberMappingCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChatroomMemberMapping> findChatroomMemberMappingWithMemberAndChatroomByMemberId(Long memberId, ChatroomType chatroomType) {
        return queryFactory.selectFrom(chatroomMemberMapping)
                .join(chatroomMemberMapping.member).fetchJoin()
                .join(chatroomMemberMapping.chatroom, chatroom).fetchJoin()
                .where(
                    chatroomMemberMapping.member.id.eq(memberId)
                    .and(chatroom.type.eq(chatroomType))
                )
                .fetch();
    }

    @Override
    public List<ChatroomMemberMapping> findChatroomMemberMappingWithMemberByChatroomId(Long chatroomId) {
        return queryFactory.selectFrom(chatroomMemberMapping)
            .join(chatroomMemberMapping.member).fetchJoin()
            .where(
                chatroomMemberMapping.chatroom.id.eq(chatroomId)
            )
            .fetch();
    }
}
