package com.danjitalk.danjitalk.common.util;

import com.danjitalk.danjitalk.common.security.JwtSecretKeys;
import com.danjitalk.danjitalk.domain.user.member.entity.SystemUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtSecretKeys jwtSecretKeys;

    private static final long SECOND = 1000;
    private static final long MINUTE = SECOND * 60;
    private static final long HOUR = MINUTE * 60;
    private static final long DAY = HOUR * 24;

    private static final long ACCESS_TOKEN_VALIDITY_TIME = 15 * MINUTE; // 15분
    private static final long REFRESH_TOKEN_VALIDITY_TIME = 12 * HOUR;  // 12시간

    private static final long ACCESS_TOKEN_COOKIE_VALIDITY_TIME = ACCESS_TOKEN_VALIDITY_TIME / 1000;
    private static final long REFRESH_TOKEN_COOKIE_VALIDITY_TIME = REFRESH_TOKEN_VALIDITY_TIME / 1000;

    public String createAccessToken(SystemUser user) {
        return Jwts.builder()
            .subject("accessToken")
            .claims(createAccessTokenClaims(user))
            .expiration(createTokenExpiration(ACCESS_TOKEN_VALIDITY_TIME))
            .signWith(createSigningKey(jwtSecretKeys.getAccessSecret()))
            .compact();
    }

    public String createAccessToken(Map<String, Object> claims) {
        return Jwts.builder()
            .subject("accessToken")
            .claims(claims)
            .expiration(createTokenExpiration(ACCESS_TOKEN_VALIDITY_TIME))
            .signWith(createSigningKey(jwtSecretKeys.getAccessSecret()))
            .compact();
    }

    public String createRefreshToken(SystemUser user) {
        return Jwts.builder()
            .subject("refreshToken")
            .claims(createRefreshTokenClaims(user))
            .expiration(createTokenExpiration(REFRESH_TOKEN_VALIDITY_TIME))
            .signWith(createSigningKey(jwtSecretKeys.getRefreshSecret()))
            .compact();
    }

    public String createRefreshToken(Map<String, Object> claims) {
        return Jwts.builder()
            .subject("refreshToken")
            .claims(claims)
            .expiration(createTokenExpiration(REFRESH_TOKEN_VALIDITY_TIME))
            .signWith(createSigningKey(jwtSecretKeys.getRefreshSecret()))
            .compact();
    }

    public ResponseCookie generateAccessTokenCookie(String accessToken) {
        return ResponseCookie.from("access", accessToken)
            .httpOnly(true)
            .sameSite("None")
            .secure(true)
            .path("/")
            .maxAge(ACCESS_TOKEN_COOKIE_VALIDITY_TIME)
            .build();
    }

    public ResponseCookie generateRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refresh", refreshToken)
            .httpOnly(true)
            .sameSite("None")
            .secure(true)
            .path("/")
            .maxAge(REFRESH_TOKEN_COOKIE_VALIDITY_TIME)
            .build();
    }


    private Date createTokenExpiration(long expirationTime) {
        return new Date(System.currentTimeMillis() + expirationTime);
    }

    // BASE64로 인코딩된 문자열을 디코딩하여 대칭키를 생성한다. 이 키는 JWT 서명 과정에서 사용된다.
    public Key createSigningKey(String base64EncodedSecretKey) {
        // 입력된 tokenSecret은 BASE64로 인코딩되어 있으므로, 먼저 디코딩하여 원래의 바이트 배열 형태로 복원한다.
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Map<String, Object> createAccessTokenClaims(SystemUser user) {
        Map<String, Object> map = new HashMap<>();
        map.put("memberId", user.getSystemUserId());
        map.put("memberEmail", user.getLoginId());
        map.put("provider", user.getLastLoginMethod());
        return map;
    }

    private Map<String, Object> createRefreshTokenClaims(SystemUser user) {
        Map<String, Object> map = new HashMap<>();
        map.put("memberEmail", user.getLoginId());
        return map;
    }
}
