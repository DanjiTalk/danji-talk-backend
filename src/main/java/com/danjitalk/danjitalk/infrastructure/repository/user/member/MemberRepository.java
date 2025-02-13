package com.danjitalk.danjitalk.infrastructure.repository.user.member;

import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
