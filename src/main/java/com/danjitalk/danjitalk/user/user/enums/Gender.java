package com.danjitalk.danjitalk.user.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum Gender {

    MALE("남성"),
    FEMALE("여성");

    private final String description;
}
