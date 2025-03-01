package com.danjitalk.danjitalk.domain.community.feed.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeedType {

    FEED("일반글"),
    NOTICE("공지글");

    private final String description;
}
