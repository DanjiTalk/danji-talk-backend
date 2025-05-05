package com.danjitalk.danjitalk.domain.bookmark.dto;

import java.time.LocalDateTime;

//피드 정보(id, 제목, 내용, 썸네일, 작성자?, 작성일 등)
public record FeedBookmarkResponse(
    Long bookmarkId,

    Long feedId,
    String title,
    String contents,
    String thumbnailFileUrl,
    LocalDateTime createdAt,
    Integer viewCount,
    Integer commentCount,
    Integer reactionCount,
    Integer bookmarkCount,

    Long memberId,
    String nickname
) implements IBookmarkResponse {

}
