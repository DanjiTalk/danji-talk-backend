package com.danjitalk.danjitalk.infrastructure.repository.user.member;

import com.danjitalk.danjitalk.domain.user.member.entity.Member;

import java.util.Optional;

public interface MemberCustomRepository {

    /**
     * SystemUserId 로 Member entity 조회
     * */
    Optional<Member> findMemberFetchJoinBySystemUserId(Long systemUserId);
}
