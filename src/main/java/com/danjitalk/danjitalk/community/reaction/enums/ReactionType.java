package com.danjitalk.danjitalk.community.reaction.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReactionType {

    LIKE("좋아요");

    private final String description;
}
