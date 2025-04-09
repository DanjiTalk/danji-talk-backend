package com.danjitalk.danjitalk.domain.community.feed.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeedSortType {

    ALL("전체"),
    LATEST("최신순"),
    POPULAR("인기순");

    private final String description;
}
