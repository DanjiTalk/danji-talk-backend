package com.danjitalk.danjitalk.common.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final CustomMemberDetailsService customMemberDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("authenticate");

        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        String email = token.getName();
        String password = token.getCredentials().toString();

        CustomMemberDetails loadedMember = (CustomMemberDetails) customMemberDetailsService.loadUserByUsername(email);

        if (!passwordEncoder.matches(password, loadedMember.getPassword())) {
            throw new BadCredentialsException("로그인 정보가 올바르지 않습니다.");
        }

        // credentials(password) 알아서 비워서 전달됨
        return new UsernamePasswordAuthenticationToken(loadedMember, password, loadedMember.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }

}
