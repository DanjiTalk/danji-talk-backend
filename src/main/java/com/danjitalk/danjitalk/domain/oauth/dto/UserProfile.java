package com.danjitalk.danjitalk.domain.oauth.dto;

import lombok.Builder;

@Builder
public record UserProfile(
     String username, // 사용자 이름
     String provider, // 로그인한 서비스
     String email, // 사용자의 이메일
     String profile
) {

}
