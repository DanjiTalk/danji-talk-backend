package com.danjitalk.danjitalk.infrastructure.repository.community.feed.impl;

import com.danjitalk.danjitalk.domain.community.feed.entity.Feed;
import com.danjitalk.danjitalk.infrastructure.repository.community.feed.FeedCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.danjitalk.danjitalk.domain.community.feed.entity.QFeed.feed;
import static com.danjitalk.danjitalk.domain.user.member.entity.QMember.member;

@RequiredArgsConstructor
public class FeedCustomRepositoryImpl implements FeedCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Feed> findFeedFetchJoinMemberByFeedId(Long feedId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(feed)
                .join(feed.member, member).fetchJoin()
                .where(feed.id.eq(feedId))
                .fetchOne()
        );
    }
}
