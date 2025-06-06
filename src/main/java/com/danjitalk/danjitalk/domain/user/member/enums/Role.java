package com.danjitalk.danjitalk.domain.user.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum Role {

    USER("사용자"),
    ADMIN("어드민");

    private final String description;
}
