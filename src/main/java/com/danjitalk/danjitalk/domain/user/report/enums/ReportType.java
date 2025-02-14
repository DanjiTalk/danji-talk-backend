package com.danjitalk.danjitalk.domain.user.report.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportType {

    FEED("게시글"),
    COMMENT("댓글"),
    CHAT("채팅");

    private final String description;
}
