package com.danjitalk.danjitalk.domain.s3.enums;

import com.danjitalk.danjitalk.domain.community.feed.enums.FeedType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileType {

    FEED(FeedType.FEED),
    NOTICE(FeedType.NOTICE),
    APARTMENT(null),
    MEMBER(null);

    private final FeedType feedType;

    public static FileType fromFeedType(FeedType feedType) {
        if (feedType == null) {
            throw new IllegalArgumentException("FeedType이 null일 수 없습니다.");
        }

        for (FileType fileType : values()) {
            if (fileType.getFeedType() != null && fileType.getFeedType() == feedType) {
                return fileType;
            }
        }
        throw new IllegalArgumentException("해당 FeedType에 매칭되는 FileType이 없습니다: " + feedType);
    }
}
