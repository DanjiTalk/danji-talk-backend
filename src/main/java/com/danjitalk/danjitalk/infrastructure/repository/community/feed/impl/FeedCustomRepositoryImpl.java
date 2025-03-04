package com.danjitalk.danjitalk.infrastructure.repository.community.feed.impl;

import com.danjitalk.danjitalk.domain.community.feed.dto.request.GetFeedListRequestDto;
import com.danjitalk.danjitalk.domain.community.feed.dto.response.ProjectionFeedDto;
import com.danjitalk.danjitalk.domain.community.feed.dto.response.QProjectionFeedListResponseDto;
import com.danjitalk.danjitalk.domain.community.feed.entity.Feed;
import com.danjitalk.danjitalk.infrastructure.repository.community.feed.FeedCustomRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
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

    @Override
    public Optional<List<ProjectionFeedDto>> getProjectionFeedList(GetFeedListRequestDto requestDto) {

        return Optional.ofNullable(queryFactory.select(
                new QProjectionFeedListResponseDto(
                        feed.id.as("feedId"),
                        member.id.as("memberId"),
                        member.nickname.as("nickName"),
                        feed.title.as("title"),
                        feed.contents.as("contents"),
                        feed.createdAt.as("localDateTime"),
                        feed.reactionCount.as("reactionCount"),
                        feed.commentCount.as("commentCount"),
                        Expressions.nullExpression()
                ))
                .from(feed)
                .leftJoin(feed.member, member)
                .where(
                        apartmentIdEq(requestDto.apartmentId()),
                        cursorDateGt(requestDto.cursorDate())
                )
                .orderBy(feed.createdAt.desc())
                .limit(15)
                .fetch());
    }

    private BooleanExpression apartmentIdEq(Long apartmentId) {
        return apartmentId != null ? feed.apartment.id.eq(apartmentId) : null;
    }

    private BooleanExpression cursorDateGt(LocalDateTime cursorDate) {
        return cursorDate != null ? feed.createdAt.lt(cursorDate) : null;
    }
}
