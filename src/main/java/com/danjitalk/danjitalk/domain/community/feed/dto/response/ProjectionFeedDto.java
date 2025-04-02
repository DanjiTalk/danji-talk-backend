package com.danjitalk.danjitalk.domain.community.feed.dto.response;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;

public record ProjectionFeedDto(
        Long feedId,
        Long memberId,
        String nickName,
        String title,
        String contents,
        LocalDateTime localDateTime,
        Integer reactionCount,
        Integer commentCount,
        Integer bookmarkCount,
        String thumbnailFileUrl,
        Boolean isReacted
) {

    @QueryProjection
    public ProjectionFeedDto(
            Long feedId, Long memberId, String nickName, String title, String contents, LocalDateTime localDateTime, Integer reactionCount, Integer commentCount, Integer bookmarkCount, String thumbnailFileUrl, Boolean isReacted
    ) {
        this.feedId = feedId;
        this.memberId = memberId;
        this.nickName = nickName;
        this.title = title;
        this.contents = contents;
        this.localDateTime = localDateTime;
        this.reactionCount = reactionCount;
        this.commentCount = commentCount;
        this.bookmarkCount = bookmarkCount;
        this.thumbnailFileUrl = thumbnailFileUrl;
        this.isReacted = isReacted;
    }
}
