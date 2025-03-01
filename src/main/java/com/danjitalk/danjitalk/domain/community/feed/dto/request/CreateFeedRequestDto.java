package com.danjitalk.danjitalk.domain.community.feed.dto.request;

import com.danjitalk.danjitalk.domain.community.feed.entity.Feed;
import com.danjitalk.danjitalk.domain.community.feed.enums.FeedType;

public record CreateFeedRequestDto(
        String title,
        String contents,
        FeedType feedType
) {

    /**
     * 게시글 작성 시 dto -> entity
     * @param createFeedRequestDto - 게시글 생성 DTO
     * @return Feed - 게시글 entity
     * */
    public Feed toFeed(CreateFeedRequestDto createFeedRequestDto) {
        return new Feed(createFeedRequestDto.title, createFeedRequestDto.contents, createFeedRequestDto.feedType);
    }
}
