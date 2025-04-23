package com.danjitalk.danjitalk.domain.user.member.dto.request;

public record UpdateMemberRequestDto(
        String password,
        String name,
        String nickname,
        String phoneNumber
) {
}
