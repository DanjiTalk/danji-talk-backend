package com.danjitalk.danjitalk.application.chat;

import com.danjitalk.danjitalk.common.security.CustomMemberDetails;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
@Slf4j
public class CustomHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
        Map<String, Object> attributes) throws Exception {
        log.info("before handshake");

        // 인증 실패 시 false 반환 핸드셰이크 실패처리
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("authentication: {}", authentication);
        // 인증이 없거나 인증되지 않은 경우
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("authentication is not authenticated");
            return false;
        }

        // principal이 CustomMemberDetails 타입이 아닌 경우
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomMemberDetails)) {
            log.warn("principal is not valid");
            return false;
        }

//        HttpHeaders headers = request.getHeaders();
//        String cookieHeader = headers.getFirst(HttpHeaders.COOKIE); // 쿠키 헤더 가져오기, 쿠키 ;로 나눠서 가져옴
//        log.info("cookieHeader: {}", cookieHeader);
//
//        if(cookieHeader != null && !cookieHeader.isEmpty()) { // String.isEmpty -> ""길이0인 문자열
//            String[] cookies = cookieHeader.split(";");
//            for(String cookie : cookies) {
//                cookie = cookie.trim();
//                if (cookie.startsWith("access=")) {
//                    cookie = cookie.substring(7);
//                    attributes.put("access", cookie);
//                }
//                log.info("cookie: {}", cookie);  // StompHeaderAccessor의 simpSessionAttributes에 넣음
//            }
//        }
//        log.info("attributes: {}", attributes); // 이후 ChannelInterceptor에 simpSessionAttributes에서 사용
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
        Exception exception) {

    }
}
