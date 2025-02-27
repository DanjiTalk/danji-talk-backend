package com.danjitalk.danjitalk.domain.user.member.dto.request;

public record ResetPasswordRequest(
    String email,
    String password
) {

}
