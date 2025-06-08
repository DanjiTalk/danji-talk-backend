package com.danjitalk.danjitalk.api.oauth;

import com.danjitalk.danjitalk.common.response.ApiResponse;
import com.danjitalk.danjitalk.common.security.CustomMemberDetails;
import com.danjitalk.danjitalk.common.util.JwtUtil;
import com.danjitalk.danjitalk.domain.user.member.entity.SystemUser;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OAuthController {

    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtUtil jwtUtil;

    @GetMapping("/api/success/oauth")
    public ResponseEntity successOAuth(@AuthenticationPrincipal CustomMemberDetails memberDetails) {
        memberDetails.getEmail();
        return ResponseEntity.ok(memberDetails.getEmail());
    }

    @GetMapping("/api/oauth/exchange")
    public ResponseEntity<Void> oauthExchange(HttpServletResponse response, @RequestParam String code) {
        log.info("oauthExchange: {}", code);

        String key = "jwt:temp:claim:" + code;
        Map<String, Object> data = (Map<String, Object>) redisTemplate.opsForValue().get(key);

        Map<String, Object> accessClaims = new HashMap<>();
        accessClaims.put("memberId", data.get("memberId"));
        accessClaims.put("memberEmail", data.get("memberEmail"));
        accessClaims.put("provider", data.get("provider"));

        Map<String, Object> refreshClaims = new HashMap<>();
        refreshClaims.put("memberEmail", data.get("memberEmail"));

        String accessToken = jwtUtil.createAccessToken(accessClaims);
        String refreshToken = jwtUtil.createRefreshToken(refreshClaims);

        ResponseCookie accessTokenCookie = jwtUtil.generateAccessTokenCookie(accessToken); // localhost로 하면 아예 쿠키 저장안됌
        ResponseCookie refreshTokenCookie = jwtUtil.generateRefreshTokenCookie(refreshToken);

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/ws/token")
    public ResponseEntity<ApiResponse<String>> beforeHandshakeTempUUIDToken(@AuthenticationPrincipal CustomMemberDetails memberDetails) {
        SystemUser user = memberDetails.getUser();

        Map<String, Object> map = new HashMap<>();
        map.put("memberId", user.getMember().getId());
        map.put("memberEmail", user.getLoginId());
        map.put("provider", user.getLastLoginMethod());
        map.put("userId", user.getSystemUserId());

        String uuid = UUID.randomUUID().toString();
        String key = "ws:temp:claim:" + uuid;

        redisTemplate.opsForValue().set(key, map, Duration.ofMinutes(3));
        return ResponseEntity.ok(ApiResponse.success(200, null, uuid));
    }
}
