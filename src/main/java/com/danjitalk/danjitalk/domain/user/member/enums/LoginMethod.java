package com.danjitalk.danjitalk.domain.user.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum LoginMethod {

    NORMAL("기본 로그인"),
    KAKAO("카카오 로그인"),
    GOOGLE("구글 로그인"),
    NAVER("네이버 로그인");

    private final String description;
}
