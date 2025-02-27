package com.danjitalk.danjitalk.infrastructure.repository.user.member.impl;

import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import com.danjitalk.danjitalk.infrastructure.repository.user.member.MemberCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.danjitalk.danjitalk.domain.user.member.entity.QMember.member;
import static com.danjitalk.danjitalk.domain.user.member.entity.QSystemUser.systemUser;

@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Member> findMemberFetchJoinBySystemUserId(Long systemUserId) {

        return Optional.ofNullable(queryFactory
                .select(member)
                .from(systemUser)
                .join(systemUser.member, member)
                        .where(systemUser.systemUserId.eq(systemUserId))
                .fetchOne()
        );
    }
}
