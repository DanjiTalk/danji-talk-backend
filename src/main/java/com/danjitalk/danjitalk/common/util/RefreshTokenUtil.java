package com.danjitalk.danjitalk.common.util;


import com.danjitalk.danjitalk.common.security.JwtSecretKeys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenUtil {

    private final JwtSecretKeys jwtSecretKeys;
    private final JwtUtil jwtUtil;

    public Claims getClaimsFromRefreshToken(String refreshToken) {
        return Jwts.parser().verifyWith((SecretKey) jwtUtil.createSigningKey(jwtSecretKeys.getRefreshSecret())).build().parseSignedClaims(refreshToken).getPayload();
    }

    public static String extractRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public boolean checkIfRefreshTokenValid(String refreshToken) {
        Claims claims = null;

        try {
            claims = getClaimsFromRefreshToken(refreshToken);
        } catch (ExpiredJwtException e) { // 리프레시 토큰 만료
            return false;
        } catch (MalformedJwtException e) { // 위변조 검사
            return false;
        } catch (JwtException e) { // 어떤 형태든 리프레쉬가 비정상이면 해줄 게 없음
            return false;
        }
        return true;
    }

}
