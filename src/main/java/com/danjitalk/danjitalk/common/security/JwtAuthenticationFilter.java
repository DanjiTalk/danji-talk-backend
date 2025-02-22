package com.danjitalk.danjitalk.common.security;

import com.danjitalk.danjitalk.common.response.ApiResponse;
import com.danjitalk.danjitalk.common.util.JwtUtil;
import com.danjitalk.danjitalk.common.util.ResponseUtil;
import com.danjitalk.danjitalk.domain.user.member.entity.SystemUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter  {

    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper, JwtUtil jwtUtil) {
        super("/api/login", authenticationManager);
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException, IOException {

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            // body로 넘어온 값으로 systemUser 객체를 생성
            SystemUser systemUser = objectMapper.readValue(request.getReader(), SystemUser.class);

            UsernamePasswordAuthenticationToken userToken =
                new UsernamePasswordAuthenticationToken(systemUser.getLoginId(), systemUser.getPassword());

            this.setDetails(request, userToken);

            // AuthenticationManager에 인증을 위임
            return super.getAuthenticationManager().authenticate(userToken);
        }
    }

    // UsernamePasswordAuthenticationFilter 메소드와 동일
    private void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(super.authenticationDetailsSource.buildDetails(request));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
        Authentication authResult) throws IOException, ServletException {
        log.info("successfulAuthentication");
        // 1. 로그인 성공된 유저 조회
        SystemUser systemUser = ((CustomMemberDetails) authResult.getPrincipal()).getUser();

        String refreshToken = jwtUtil.createRefreshToken(systemUser);

        ResponseCookie refreshTokenCookie = jwtUtil.generateRefreshTokenCookie(refreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        String accessToken = jwtUtil.createAccessToken(systemUser);

        ResponseCookie accessTokenCookie = jwtUtil.generateAccessTokenCookie(accessToken);
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

        ResponseUtil.createResponseBody(response, HttpStatus.OK);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException failed) throws IOException, ServletException {
        log.info("unsuccessfulAuthentication");
        ApiResponse<Void> errorResponse = ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), failed.getMessage());
        ResponseUtil.createResponseBody(response, HttpStatus.BAD_REQUEST, errorResponse);
    }
}
