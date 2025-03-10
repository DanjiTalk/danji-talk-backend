package com.danjitalk.danjitalk.infrastructure.repository.community.comment;

import com.danjitalk.danjitalk.domain.community.comment.entity.Comment;
import com.danjitalk.danjitalk.domain.community.comment.entity.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.danjitalk.danjitalk.domain.community.comment.entity.QComment.comment;
import static com.danjitalk.danjitalk.domain.community.feed.entity.QFeed.feed;
import static com.danjitalk.danjitalk.domain.user.member.entity.QMember.member;

@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Comment> findAllByFeedId(Long feedId, Pageable pageRequest) {

        QComment childComment = new QComment("childComment");

        // 부모 댓글만 가져옴
        List<Comment> fetch = queryFactory.selectFrom(comment)
                .join(comment.feed, feed).fetchJoin()
                .join(comment.member, member).fetchJoin()
                .leftJoin(comment.childrenList, childComment).fetchJoin()
                .where(comment.feed.id.eq(feedId).and(comment.parent.isNull()))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        long total = Optional.ofNullable(queryFactory.select(comment.count())
                .from(comment)
                .where(comment.feed.id.eq(feedId).and(comment.parent.isNull()))
                .fetchOne()).orElseGet(() -> 0L);

        return new PageImpl<>(fetch, pageRequest, total);
    }
}
