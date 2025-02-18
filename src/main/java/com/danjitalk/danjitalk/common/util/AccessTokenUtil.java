package com.danjitalk.danjitalk.common.util;

import static com.danjitalk.danjitalk.common.util.JwtUtil.createSigningKey;

import com.danjitalk.danjitalk.common.security.JwtSecretKeys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import javax.crypto.SecretKey;

public class AccessTokenUtil {

    public static Claims getClaimsFromAccessToken(String accessToken) {
        return Jwts.parser().verifyWith((SecretKey) createSigningKey(JwtSecretKeys.getAccessSecret())).build().parseSignedClaims(accessToken).getPayload();
    }

    public static String extractAccessTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null; // 해당 이름의 쿠키가 없을 경우 null을 반환
    }
}
