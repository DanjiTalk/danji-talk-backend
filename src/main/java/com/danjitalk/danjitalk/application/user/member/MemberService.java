package com.danjitalk.danjitalk.application.user.member;

import static com.danjitalk.danjitalk.common.util.SecurityContextHolderUtil.getSystemUser;

import com.danjitalk.danjitalk.common.exception.ConflictException;
import com.danjitalk.danjitalk.common.exception.DataNotFoundException;
import com.danjitalk.danjitalk.domain.user.member.dto.request.CheckEmailDuplicationRequest;
import com.danjitalk.danjitalk.domain.user.member.dto.request.DeleteAccountRequest;
import com.danjitalk.danjitalk.domain.user.member.dto.request.FindIdRequest;
import com.danjitalk.danjitalk.domain.user.member.dto.request.ResetPasswordRequest;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final SystemUserRepository systemUserRepository;
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
                .nickname(request.nickname())
                .phoneNumber(request.phoneNumber())
                .notificationEnabled(null)
                .isRestricted(false)
                .restrictionTime(null)
                .fileId(null)
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

    @Transactional(readOnly = true)
    public void isEmailAvailable(CheckEmailDuplicationRequest request) {
        boolean isEmailExist = memberRepository.existsByEmail(request.email());
        if(isEmailExist){
            throw new ConflictException("이미 등록된 이메일 입니다.");
        }
    }

    @Transactional
    public void deleteMember(DeleteAccountRequest request) {
        SystemUser systemUser = getSystemUser(); //  현재 로그인 정보 == 지우려는 로그인정보

        if (!passwordEncoder.matches(request.password(), systemUser.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        memberRepository.delete(systemUser.getMember());
        systemUserRepository.delete(systemUser);
    }

    public String findMemberId(FindIdRequest request) {
        return memberRepository.findByNameAndPhoneNumber(request.name(), request.phoneNumber())
            .orElseThrow(DataNotFoundException::new)
            .getEmail();
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        SystemUser systemUser = systemUserRepository.findByLoginId(request.email()).orElseThrow(DataNotFoundException::new);
        String encodedPassword = passwordEncoder.encode(request.password());
        systemUser.updatePassword(encodedPassword);
    }
}
