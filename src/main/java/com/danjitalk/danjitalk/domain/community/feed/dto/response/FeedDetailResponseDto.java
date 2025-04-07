package com.danjitalk.danjitalk.domain.community.feed.dto.response;

import com.danjitalk.danjitalk.domain.s3.dto.response.S3ObjectResponseDto;
import com.danjitalk.danjitalk.domain.user.member.dto.response.FeedMemberResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public record FeedDetailResponseDto(
        Long feedId,
        String title,
        String contents,
        LocalDateTime createdAt,
        FeedMemberResponseDto feedMemberResponseDto,
        List<S3ObjectResponseDto> s3ObjectResponseDtoList,
        Integer viewCount,
        Integer reactionCount,
        Integer commentCount,
        Integer bookmarkCount,
        Boolean isReacted,
        Boolean isBookmarked
        )
{
}
