package com.danjitalk.danjitalk.application.user.member;

import com.danjitalk.danjitalk.common.exception.ConflictException;
import com.danjitalk.danjitalk.domain.user.member.dto.request.SignUpRequest;
import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import com.danjitalk.danjitalk.domain.user.member.entity.SystemUser;
import com.danjitalk.danjitalk.domain.user.member.enums.LoginMethod;
import com.danjitalk.danjitalk.domain.user.member.enums.Role;
import com.danjitalk.danjitalk.domain.user.member.service.MemberDomainService;
import com.danjitalk.danjitalk.infrastructure.repository.user.member.MemberRepository;
import com.danjitalk.danjitalk.infrastructure.repository.user.member.SystemUserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final SystemUserRepository systemUserRepository;
    private final MemberDomainService memberDomainService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpRequest request) {
        Optional<Member> existingMember = memberRepository.findByEmailOrPhoneNumber(request.email(), request.phoneNumber());

        existingMember.ifPresent(member -> {
            if (member.getEmail().equals(request.email())) {
                throw new ConflictException("이메일 중복");
            }
            if (member.getPhoneNumber().equals(request.phoneNumber())) {
                throw new ConflictException("이미 가입한 번호");
            }
        });

        Member member = saveMember(request);
        saveSystemUser(request, member);
    }

    private Member saveMember(SignUpRequest request) {
        Member member = Member.builder()
                .email(request.email())
                .name(request.name())
                .birthDate(request.birthDate())
                .age(request.age())
                .phoneNumber(request.phoneNumber())
                .notificationEnabled(request.notificationEnabled())
                .isRestricted(false)
                .restrictionTime(null)
                .fileId(request.fileId())
                .build();

        return memberRepository.save(member);
    }

    private void saveSystemUser(SignUpRequest request, Member member) {
        String encodedPassword = passwordEncoder.encode(request.password());

        SystemUser systemUser = SystemUser.builder()
                .loginId(request.email())
                .password(encodedPassword)
                .lastLoginTime(LocalDateTime.now())
                .lastLoginMethod(LoginMethod.NORMAL)
                .role(Role.USER)
                .member(member)
                .build();

        systemUserRepository.save(systemUser);
    }
}
