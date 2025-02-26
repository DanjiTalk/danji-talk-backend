package com.danjitalk.danjitalk.application.oauth;

import com.danjitalk.danjitalk.common.security.CustomMemberDetails;
import com.danjitalk.danjitalk.common.util.JwtUtil;
import com.danjitalk.danjitalk.domain.user.member.entity.SystemUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        // OAuth2 로그인이 성공했을 때의 추가 작업을 수행
        // 여기에서는 JWT 토큰을 발급하고 형식에 맞게 return
        CustomMemberDetails customMemberDetails = (CustomMemberDetails) authentication.getPrincipal();

        issueAccessTokenCookie(customMemberDetails.getUser(), response);
        issueRefreshTokenCookie(customMemberDetails.getUser(), response);
    }

    private void issueAccessTokenCookie(SystemUser systemUser, HttpServletResponse response) {
        String accessToken = jwtUtil.createAccessToken(systemUser);
        ResponseCookie accessTokenCookie = jwtUtil.generateAccessTokenCookie(accessToken);
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
    }

    private void issueRefreshTokenCookie(SystemUser systemUser, HttpServletResponse response) {
        String refreshToken = jwtUtil.createRefreshToken(systemUser);
        ResponseCookie refreshTokenCookie = jwtUtil.generateRefreshTokenCookie(refreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }
}
