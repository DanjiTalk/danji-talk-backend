package com.danjitalk.danjitalk.infrastructure.repository.bookmark.impl;

import com.danjitalk.danjitalk.domain.bookmark.dto.ApartmentBookmarkResponse;
import com.danjitalk.danjitalk.domain.bookmark.dto.IBookmarkResponse;
import com.danjitalk.danjitalk.domain.bookmark.dto.FeedBookmarkResponse;
import com.danjitalk.danjitalk.domain.bookmark.enums.BookmarkType;
import com.danjitalk.danjitalk.infrastructure.repository.bookmark.BookmarkCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;

import static com.danjitalk.danjitalk.domain.bookmark.entity.QBookmark.bookmark;
import static com.danjitalk.danjitalk.domain.community.feed.entity.QFeed.feed;
import static com.danjitalk.danjitalk.domain.apartment.entity.QApartment.apartment;
import static com.danjitalk.danjitalk.domain.user.member.entity.QMember.member;

@RequiredArgsConstructor
public class BookmarkCustomRepositoryImpl implements BookmarkCustomRepository {

    private final JPAQueryFactory queryFactory;

    // A Type: 아파트 정보(id, 이름, 지역, 아파트 세대, 동 수, 아파트 썸네일, 북마크 여부)
    // B Type: 피드 정보(id, 게시글 제목, 내용 썸네일, 작성자?, 작성일 등)
    @Override
    public List<IBookmarkResponse> findByTypeAndMemberIdWithCursor(BookmarkType bookmarkType, Long memberId, Long cursor, Long limit) {
        if (bookmarkType == BookmarkType.FEED) {
            List<FeedBookmarkResponse> feedBookmarks = queryFactory
                .select(Projections.constructor(FeedBookmarkResponse.class,
                    bookmark.id,

                    feed.id,
                    feed.title,
                    feed.contents,
                    feed.thumbnailFileUrl,
                    feed.createdAt,
                    feed.viewCount,
                    feed.commentCount,
                    feed.reactionCount,
                    feed.bookmarkCount,

                    member.id,
                    member.nickname
                ))
                .from(bookmark)
                .join(feed).on(bookmark.typeId.eq(feed.id))
                .join(member).on(bookmark.memberId.eq(member.id))
                .where(
                    bookmark.type.eq(bookmarkType),
                    bookmark.memberId.eq(memberId),
                    ltCursor(cursor)
                )
                .orderBy(bookmark.id.desc())
                .limit(limit)
                .fetch();

             return new ArrayList<>(feedBookmarks);
        }

        if(bookmarkType == BookmarkType.APARTMENT) {
            List<ApartmentBookmarkResponse> apartmentBookmarks = queryFactory
                .select(Projections.constructor(ApartmentBookmarkResponse.class,
                    bookmark.id,

                    apartment.id,
                    apartment.name,
                    apartment.region,
                    apartment.location,
                    apartment.totalUnit,
                    apartment.buildingCount,
                    apartment.thumbnailFileUrl
                ))
                .from(bookmark)
                .join(apartment).on(apartment.id.eq(bookmark.typeId))
                .join(member).on(bookmark.memberId.eq(member.id))
                .where(
                    bookmark.type.eq(bookmarkType),
                    bookmark.memberId.eq(memberId),
                    ltCursor(cursor)
                )
                .limit(limit)
                .orderBy(bookmark.id.desc())
                .fetch();

            return new ArrayList<>(apartmentBookmarks);
        }

        return Collections.emptyList();
    }

    private BooleanExpression ltCursor(Long cursor) {
        if (cursor == null) {
            return null;
        }

        return bookmark.id.lt(cursor);
    }
}
