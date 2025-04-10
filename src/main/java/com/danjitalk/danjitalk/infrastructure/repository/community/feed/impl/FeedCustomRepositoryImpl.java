package com.danjitalk.danjitalk.infrastructure.repository.community.feed.impl;

import com.danjitalk.danjitalk.common.exception.BadRequestException;
import com.danjitalk.danjitalk.domain.community.feed.dto.response.ProjectionFeedDto;
import com.danjitalk.danjitalk.domain.community.feed.dto.response.QProjectionFeedDto;
import com.danjitalk.danjitalk.domain.community.feed.entity.Feed;
import com.danjitalk.danjitalk.domain.community.feed.enums.FeedSortType;
import com.danjitalk.danjitalk.infrastructure.repository.community.feed.FeedCustomRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.danjitalk.danjitalk.domain.community.feed.entity.QFeed.feed;
import static com.danjitalk.danjitalk.domain.community.reaction.entity.QReaction.reaction;
import static com.danjitalk.danjitalk.domain.user.member.entity.QMember.member;

@RequiredArgsConstructor
public class FeedCustomRepositoryImpl implements FeedCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Feed> findFeedFetchJoinMemberByFeedId(Long feedId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(feed)
                .join(feed.member, member).fetchJoin()
                .leftJoin(feed.reactionList, reaction).fetchJoin()
                .where(feed.id.eq(feedId))
                .fetchOne()
        );
    }

    @Override
    public Optional<List<ProjectionFeedDto>> getProjectionFeedList(Long apartmentId, LocalDateTime cursorDate, FeedSortType sort) {

        return Optional.ofNullable(queryFactory.select(
                new QProjectionFeedDto(
                        feed.id.as("feedId"),
                        member.id.as("memberId"),
                        member.nickname.as("nickName"),
                        feed.title.as("title"),
                        feed.contents.as("contents"),
                        feed.createdAt.as("localDateTime"),
                        feed.viewCount.as("viewCount"),
                        feed.reactionCount.as("reactionCount"),
                        feed.commentCount.as("commentCount"),
                        feed.bookmarkCount.as("bookmarkCount"),
                        feed.thumbnailFileUrl.as("thumbnailFileUrl"),
                        JPAExpressions
                                .selectOne()
                                .from(reaction)
                                .where(
                                        reaction.feed.id.eq(feed.id),
                                        reaction.member.id.eq(member.id)
                                ).exists().as("isReacted")
                ))
                .from(feed)
                .leftJoin(feed.member, member)
                .where(
                        apartmentIdEq(apartmentId),
                        cursorDateGt(cursorDate)
                )
                .orderBy(this.sortByParam(sort))
                .limit(15)
                .fetch());
    }

    @Override
    public Boolean isReacted(Long feedId, Long memberId) {
        return queryFactory
                .selectOne()
                .from(reaction)
                .where(reaction.feed.id.eq(feedId), reaction.member.id.eq(memberId))
                .fetchFirst() != null;
    }

    private BooleanExpression apartmentIdEq(Long apartmentId) {
        if( apartmentId == null ) {
            throw new BadRequestException("Apartment id is required");
        }
        return feed.apartment.id.eq(apartmentId);
    }

    private BooleanExpression cursorDateGt(LocalDateTime cursorDate) {
        return cursorDate != null ? feed.createdAt.loe(cursorDate) : null;
    }

    /**
     * 최신순, 인기순
     * */
    private OrderSpecifier<?> sortByParam(FeedSortType sort) {

        // 인기순이면 가중치 계산 -> (좋아요수 * 3) + (댓글수 * 2) + (북마크 * 1) + (조회수 * 0.2)
        if (Objects.requireNonNull(sort) == FeedSortType.POPULAR) {
            NumberExpression<Integer> score = feed.reactionCount.multiply(3)
                    .add(feed.commentCount.multiply(2))
                    .add(feed.bookmarkCount.multiply(1))
                    .add(feed.viewCount.doubleValue().multiply(0.2));
            return score.desc();
        }
        return feed.createdAt.desc();
    }
}
