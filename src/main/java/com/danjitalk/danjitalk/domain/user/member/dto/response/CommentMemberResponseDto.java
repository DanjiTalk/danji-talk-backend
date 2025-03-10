package com.danjitalk.danjitalk.domain.user.member.dto.response;

public record CommentMemberResponseDto(
        Long memberId,
        String nickname,
        String fileId

) {
}
