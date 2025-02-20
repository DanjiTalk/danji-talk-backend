package com.danjitalk.danjitalk.config;

import com.danjitalk.danjitalk.common.security.CustomMemberDetailsService;
import com.danjitalk.danjitalk.common.security.JwtAuthenticationFilter;
import com.danjitalk.danjitalk.common.security.JwtAuthenticationProvider;
import com.danjitalk.danjitalk.common.security.JwtAuthorizationFilter;
import com.danjitalk.danjitalk.infrastructure.repository.user.member.SystemUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomMemberDetailsService customMemberDetailsService;
    private final SystemUserRepository systemUserRepository;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(), objectMapper);
        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(systemUserRepository);

        http
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

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
                .requestMatchers("/api/**").permitAll()
            );

        http
            .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(jwtAuthorizationFilter, JwtAuthenticationFilter.class);

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
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.ignoring()
                .requestMatchers(HttpMethod.POST, "/api/member/signup")
                .requestMatchers(HttpMethod.POST, "/api/member/check-email-duplication")
                .requestMatchers(HttpMethod.POST, "/api/mail/certification-code/send"); // 시큐리티와 관련 없는(인증/인가 필요 없는) 필터를 타면 안되는 경로
        };
    }
}
