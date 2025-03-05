package com.danjitalk.danjitalk.common.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final RequestMappingHandlerMapping handlerMapping;

    @Override // 401 일때 옴
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("CustomAuthenticationEntryPoint#commence");
        log.info("response.getStatus(): {}", response.getStatus());
        log.info("URI: {}", request.getRequestURI());

        HandlerExecutionChain handler;
        try {
            handler = handlerMapping.getHandler(request);
        } catch (Exception e) {
            handler = null;
        }

        // 핸들러가 없는 경우 404 반환
        if (handler == null) {
            log.info("Not Found Handler");
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not Found");
        }
    }
}