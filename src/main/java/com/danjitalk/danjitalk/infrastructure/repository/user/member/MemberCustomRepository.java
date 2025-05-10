package com.danjitalk.danjitalk.infrastructure.repository.user.member;

import com.danjitalk.danjitalk.domain.user.member.dto.response.MypageResponse;

public interface MemberCustomRepository {

    MypageResponse getMemberInfoById(Long memberId);
}
