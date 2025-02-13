package com.danjitalk.danjitalk.domain.user.member.service;

import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import org.springframework.stereotype.Service;

@Service
public class MemberDomainService {
    private static final int ADULT_AGE = 20;


    /**
     * 회원이 성인인이 여부 판단
     * @param member - 회원 entity
     * @return true, false
     * */
    public boolean isMemberAdult(Member member) {
        if(member == null) {
            throw new IllegalArgumentException("member cannot be null");
        }

        return member.getAge() >= ADULT_AGE;
    }
}
