package com.danjitalk.danjitalk.infrastructure.repository.chat.impl;

import com.danjitalk.danjitalk.domain.chat.entity.ChatRequest;
import com.danjitalk.danjitalk.infrastructure.repository.chat.ChatRequestCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

import static com.danjitalk.danjitalk.domain.chat.entity.QChatRequest.chatRequest;

@RequiredArgsConstructor
public class ChatRequestCustomRepositoryImpl implements ChatRequestCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ChatRequest> findChatRequestWithRequesterAndReceiverById(Long id) {
        return Optional.ofNullable(queryFactory.selectFrom(chatRequest)
                .join(chatRequest.receiver).fetchJoin()
                .join(chatRequest.requester).fetchJoin()
                .where(chatRequest.id.eq(id))
                .fetchOne());
    }
}
