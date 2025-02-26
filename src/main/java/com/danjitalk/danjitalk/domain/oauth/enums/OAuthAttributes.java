package com.danjitalk.danjitalk.domain.oauth.enums;

import com.danjitalk.danjitalk.domain.oauth.dto.UserProfile;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {

    GOOGLE("google", (attribute) ->
        UserProfile.builder()
            .email((String) attribute.get("email"))
            .profile((String) attribute.get("picture"))
            .username((String) attribute.get("name"))
            .provider("google")
            .build()
    ),

    NAVER("naver", (attribute) -> {
        Map<String, String> responseValue = (Map)attribute.get("response");
        return UserProfile.builder()
            .email(responseValue.get("email"))
            .username(responseValue.get("name"))
            .profile(responseValue.get("profile_image"))
            .provider("naver")
            .build();
    }),

    KAKAO("kakao", (attribute) -> {

        Map<String, Object> account = (Map)attribute.get("kakao_account");
        Map<String, String> profile = (Map)account.get("profile");

        return UserProfile.builder()
            .email((String) account.get("email"))
            .username(profile.get("nickname"))
            .profile(profile.get("profile"))
            .provider("kakao")
            .build();
    });

    private final String registrationId; // 로그인한 서비스(ex) google, naver..)
    private final Function<Map<String, Object>, UserProfile> of; // 로그인한 사용자의 정보를 통하여 UserProfile을 가져옴

    OAuthAttributes(String registrationId, Function<Map<String, Object>, UserProfile> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public static UserProfile extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
            .filter(value -> registrationId.equals(value.registrationId))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new)
            .of.apply(attributes);
    }
}