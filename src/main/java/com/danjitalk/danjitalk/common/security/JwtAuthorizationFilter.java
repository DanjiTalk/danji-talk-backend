package com.danjitalk.danjitalk.common.security;

import static com.danjitalk.danjitalk.common.util.AccessTokenUtil.extractAccessTokenFromCookies;
import static com.danjitalk.danjitalk.common.util.RefreshTokenUtil.extractRefreshTokenFromCookies;

import com.danjitalk.danjitalk.common.exception.DataNotFoundException;
import com.danjitalk.danjitalk.common.util.AccessTokenUtil;
import com.danjitalk.danjitalk.common.util.JwtUtil;
import com.danjitalk.danjitalk.common.util.RefreshTokenUtil;
import com.danjitalk.danjitalk.common.util.ResponseUtil;
import com.danjitalk.danjitalk.domain.user.member.entity.SystemUser;
import com.danjitalk.danjitalk.infrastructure.repository.user.member.SystemUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final SystemUserRepository systemUserRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenUtil refreshTokenUtil;
    private final AccessTokenUtil accessTokenUtil;

    public JwtAuthorizationFilter(SystemUserRepository systemUserRepository, JwtUtil jwtUtil, AccessTokenUtil accessTokenUtil, RefreshTokenUtil refreshTokenUtil) {
        this.systemUserRepository = systemUserRepository;
        this.jwtUtil = jwtUtil;
        this.accessTokenUtil = accessTokenUtil;
        this.refreshTokenUtil = refreshTokenUtil;
    }

    // 1. RequestHeader 안의 엑세스 토큰 확인
    // 2. 액세스토큰이 유효하다면 -> 인증된 객체 저장하고 doFilter 수행
    // TODO: OncePerRequestFilter:shouldNotFilter 로 수정
    private static final List<RequestMatcher> excludedUrlPatterns = List.of( // 필터 적용 안할 url 지정
        new AntPathRequestMatcher("/api/login", "POST")
        new AntPathRequestMatcher("/api/login", "POST"),
        new AntPathRequestMatcher("/oauth2/authorization/*")
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        if (isExcludedUrl(request)) {
            filterChain.doFilter(request, response); // 필터 스킵, 다음 필터 실행.
            return;
        }
        log.info("JwtAuthorizationFilter#doFilterInternal");

        String accessToken = extractAccessTokenFromCookies(request);

        if (accessToken == null) { // 엑세스 쿠키 x == 엑세스 토큰 x
            // 리프레시 토큰 찾기
            String refreshToken = extractRefreshTokenFromCookies(request);

            // 리프레시도 없으면
            if(refreshToken == null) {
                // 둘다 없으면 재로그인 예외 던지기
                ResponseUtil.createResponseBody(response, HttpStatus.UNAUTHORIZED/*, "access-token, refresh-token expired"*/); //TODO: getWriter() has already been called for this response
                filterChain.doFilter(request, response);
                return;
            }

            // 리프레시는 있다면 유효성 검사 하고
            if (!refreshTokenUtil.checkIfRefreshTokenValid(refreshToken)) {
                ResponseUtil.createResponseBody(response, HttpStatus.UNAUTHORIZED, "access-token expired, refresh-token is invalid");
                filterChain.doFilter(request, response);
                return;
            }
            // 엑세스 없고, 리프레시는 정상
            // 유효성 성공 -> 리프레시 토큰 정보로 엑세스 및 리프레시 재발급
            Claims claims = refreshTokenUtil.getClaimsFromRefreshToken(refreshToken);

            String email = (String) claims.get("memberEmail");
            SystemUser systemUser = systemUserRepository.findByLoginId(email).orElseThrow(DataNotFoundException::new);

            reissueAccessToken(systemUser, response);
            reissueRefreshToken(systemUser, response);

            this.saveAuthenticationToSecurityContextHolder(systemUser);

            filterChain.doFilter(request, response);
            return;
        }

        Claims claims = null;
        SystemUser systemUser = null;

        try {
            claims = accessTokenUtil.getClaimsFromAccessToken(accessToken);
        } catch (ExpiredJwtException e) {
            // 엑세스 토큰이 만료되었을 때 리프레시 토큰이 유효하다면, 엑세스 토큰을 새로 발급해줍니다.
            String refreshToken = extractRefreshTokenFromCookies(request);

            // 리프레시 없으면
            if(refreshToken == null) {
                ResponseUtil.createResponseBody(response, HttpStatus.UNAUTHORIZED, "access-token is invalid, refresh-token expired");
                filterChain.doFilter(request, response);
                return;
            }

            if (!refreshTokenUtil.checkIfRefreshTokenValid(refreshToken)) {
                ResponseUtil.createResponseBody(response, HttpStatus.UNAUTHORIZED, "access-token is invalid, refresh-token is invalid");
                filterChain.doFilter(request, response);
                return;
            }

            claims = e.getClaims(); // 엑세스 토큰 claims

            Long memberId = ((Integer) claims.get("memberId")).longValue();
            systemUser = systemUserRepository.findById(memberId).orElseThrow(DataNotFoundException::new);

            reissueAccessToken(systemUser, response);
            reissueRefreshToken(systemUser, response);
        }

        if(systemUser == null) {
            systemUser = findMemberFromAccessTokenClaims(claims);
        }

        this.saveAuthenticationToSecurityContextHolder(systemUser);

        filterChain.doFilter(request, response);
    }

    private void reissueAccessToken(SystemUser systemUser, HttpServletResponse response) {
        String newAccessToken = jwtUtil.createAccessToken(systemUser);
        ResponseCookie newAccessTokenCookie = jwtUtil.generateAccessTokenCookie(newAccessToken);
        response.addHeader(HttpHeaders.SET_COOKIE, newAccessTokenCookie.toString());
    }

    private void reissueRefreshToken(SystemUser systemUser, HttpServletResponse response) {
        String newRefreshToken = jwtUtil.createRefreshToken(systemUser);
        ResponseCookie newRefreshTokenCookie = jwtUtil.generateRefreshTokenCookie(newRefreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, newRefreshTokenCookie.toString());
    }


    private SystemUser findMemberFromAccessTokenClaims(Claims claims) {
        return systemUserRepository.findByLoginId(claims.get("memberEmail").toString()).orElseThrow(DataNotFoundException::new);
    }

    private void saveAuthenticationToSecurityContextHolder(SystemUser systemUser) {
        CustomMemberDetails memberDetails = new CustomMemberDetails(systemUser);

        // 인가 처리가 정상적으로 완료된다면 Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            memberDetails, null, memberDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private boolean isExcludedUrl(HttpServletRequest request) {
        return excludedUrlPatterns.stream().anyMatch(pattern -> pattern.matches(request));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return super.shouldNotFilter(request);
    }
}

