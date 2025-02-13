package com.danjitalk.danjitalk.application.user.member;

import com.danjitalk.danjitalk.domain.user.member.service.MemberDomainService;
import com.danjitalk.danjitalk.infrastructure.repository.user.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberDomainService memberDomainService;

}
