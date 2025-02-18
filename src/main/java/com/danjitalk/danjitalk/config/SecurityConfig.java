package com.danjitalk.danjitalk.config;

import com.danjitalk.danjitalk.common.security.CustomMemberDetailsService;
import com.danjitalk.danjitalk.common.security.JwtAuthenticationProvider;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomMemberDetailsService customMemberDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll()
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
}
