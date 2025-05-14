package com.danjitalk.danjitalk.infrastructure.repository.user.member;

import com.danjitalk.danjitalk.domain.user.member.dto.response.MyPageResponse;

public interface MemberCustomRepository {

    MyPageResponse getMemberInfoById(Long memberId);
}
