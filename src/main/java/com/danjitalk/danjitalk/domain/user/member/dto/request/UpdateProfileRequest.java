package com.danjitalk.danjitalk.domain.user.member.dto.request;

public record UpdateProfileRequest(
    String password,
    String name,
    String nickname,
    String phoneNumber
) {

}
