package com.danjitalk.danjitalk.application.oauth;

import com.danjitalk.danjitalk.domain.oauth.enums.OAuthAttributes;
import com.danjitalk.danjitalk.domain.oauth.dto.UserProfile;
import com.danjitalk.danjitalk.common.security.CustomMemberDetails;
import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import com.danjitalk.danjitalk.domain.user.member.entity.SystemUser;
import com.danjitalk.danjitalk.domain.user.member.enums.LoginMethod;
import com.danjitalk.danjitalk.domain.user.member.enums.Role;
import com.danjitalk.danjitalk.infrastructure.repository.user.member.MemberRepository;
import com.danjitalk.danjitalk.infrastructure.repository.user.member.SystemUserRepository;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final SystemUserRepository systemUserRepository;
    private final MemberRepository memberRepository;

    // TODO: 순환참조 제거를 위해 별도의 빈으로 분리하기
    @Lazy
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 로그인을 수행한 서비스의 이름

        OAuth2User oauth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oauth2User.getAttributes(); // 사용자가 가지고 있는 정보
        UserProfile userProfile = OAuthAttributes.extract(registrationId, attributes);

        LoginMethod provider = LoginMethod.valueOf(userProfile.provider().toUpperCase());

        Optional<SystemUser> systemUser = systemUserRepository.findByLoginIdAndLastLoginMethod(userProfile.email(), provider);

        // userNameAttributeName (google: sub / naver,kakao: id) 값으로 하는게 나아보이는데 그냥 첫 인증시 토큰값으로 저장함
        String password = passwordEncoder.encode(userRequest.getAccessToken().getTokenValue());

        //이미 소셜로그인을 한적이 있는지 없는지
        if (systemUser.isEmpty()) { // 없으면
            Member member = Member.builder()
                .email(userProfile.email())
                .name(userProfile.username())
                .phoneNumber(null)
                .notificationEnabled(null)
                .isRestricted(false)
                .restrictionTime(null)
                .fileId(userProfile.profile())
                .gender(null)
                .build();

            SystemUser user = SystemUser.builder()
                .member(member)
                .loginId(userProfile.email())
                .password(password)
                .lastLoginTime(LocalDateTime.now())
                .lastLoginMethod(provider)
                .role(Role.USER)
                .build();

            memberRepository.save(member);
            systemUserRepository.save(user);

            return new CustomMemberDetails(user, oauth2User.getAttributes());
        } else {
            return new CustomMemberDetails(systemUser.get(), oauth2User.getAttributes());
        }
    }
}