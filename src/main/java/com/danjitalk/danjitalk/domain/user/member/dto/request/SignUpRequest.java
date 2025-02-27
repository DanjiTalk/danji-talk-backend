package com.danjitalk.danjitalk.domain.user.member.dto.request;

public record SignUpRequest(
    String email,
    String password,
    String name,
    String nickname,
    String phoneNumber
) {

}
