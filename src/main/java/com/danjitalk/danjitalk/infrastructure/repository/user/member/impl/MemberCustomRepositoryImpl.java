package com.danjitalk.danjitalk.infrastructure.repository.user.member.impl;

import com.danjitalk.danjitalk.infrastructure.repository.user.member.MemberCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory queryFactory;

}
