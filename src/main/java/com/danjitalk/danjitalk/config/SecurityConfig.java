package com.danjitalk.danjitalk.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.danjitalk.danjitalk.application.oauth.OAuth2LoginSuccessHandler;
import com.danjitalk.danjitalk.application.oauth.PrincipalOauth2UserService;
import com.danjitalk.danjitalk.common.security.*;
import com.danjitalk.danjitalk.common.util.AccessTokenUtil;
import com.danjitalk.danjitalk.common.util.JwtUtil;
import com.danjitalk.danjitalk.common.util.RefreshTokenUtil;
import com.danjitalk.danjitalk.infrastructure.repository.user.member.SystemUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomMemberDetailsService customMemberDetailsService;
    private final SystemUserRepository systemUserRepository;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final RefreshTokenUtil refreshTokenUtil;
    private final AccessTokenUtil accessTokenUtil;
    private final PrincipalOauth2UserService principalOauth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final RequestMappingHandlerMapping handlerMapping;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(), objectMapper, jwtUtil);
        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(systemUserRepository, jwtUtil, accessTokenUtil, refreshTokenUtil);

        http
            .cors(withDefaults()) // cors를 활성화하여 addCorsMappings이 spring security와 함께 동작하도록함
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
            .exceptionHandling(handler->handler
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint(handlerMapping)) // 401
            ); //AccessDeniedHandler 403

        http
            .logout(logout -> logout
                .logoutUrl("/api/logout")
                .logoutSuccessUrl("/api") // 호출할 api 있어야 No static resource api 오류안남
                .deleteCookies("refresh", "access") // 쿠키삭제 핸들러 추가 코드
            );

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/logout").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/member").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/community/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/community/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/community/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/oauth2/authorization/*").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().denyAll()
                );

        http
            .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(jwtAuthorizationFilter, JwtAuthenticationFilter.class);

        http
            .oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                .userService(principalOauth2UserService)
            )
            .successHandler(oAuth2LoginSuccessHandler)
//                .failureHandler() // TODO
        );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // AuthenticationManager 빈 등록, 다중 AuthenticationProvider 등록가능
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> authenticationProviders = new ArrayList<>();
        authenticationProviders.add(new JwtAuthenticationProvider(customMemberDetailsService, passwordEncoder()));

        return new ProviderManager(authenticationProviders);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // 시큐리티와 관련 없는(인증/인가 필요 없는) 필터를 타면 안되는 경로
        return web -> {
            web.ignoring()
                .requestMatchers("/api/oauth/exchange")
                .requestMatchers("/api/search/popular-keywords")
                .requestMatchers(HttpMethod.POST, "/api/member/signup")
                .requestMatchers(HttpMethod.POST, "/api/member/check-email-duplication")
                .requestMatchers(HttpMethod.POST, "/api/mail/certification-code/send")
                .requestMatchers(HttpMethod.GET, "/api/mail/certification-code/verify")
                .requestMatchers(HttpMethod.POST, "/api/mail/certification-code/verify")
                .requestMatchers(HttpMethod.POST, "/api/member/find-id")
                .requestMatchers(HttpMethod.POST, "/api/member/reset-password")
                .requestMatchers(HttpMethod.GET,"/social-login")  // TODO: social-login, favicon 삭제
                .requestMatchers(HttpMethod.GET, "/favicon.ico")
                .requestMatchers("/error");
        };
    }
}
