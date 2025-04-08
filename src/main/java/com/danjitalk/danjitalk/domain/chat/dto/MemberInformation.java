package com.danjitalk.danjitalk.domain.chat.dto;

import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import lombok.Builder;

@Builder
public record MemberInformation(
    Long id,
    String nickname,
    String profileUrl
) {

    public static MemberInformation from(Member member) {
        return MemberInformation.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .profileUrl(member.getFileId())
                .build();
    }
}
