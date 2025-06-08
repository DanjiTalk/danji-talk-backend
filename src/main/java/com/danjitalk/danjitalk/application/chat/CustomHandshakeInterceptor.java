package com.danjitalk.danjitalk.application.chat;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomHandshakeInterceptor implements HandshakeInterceptor {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
        Map<String, Object> attributes) throws Exception {
        log.info("before handshake");

        String query = request.getURI().getQuery();

        if (query == null || !query.startsWith("token=")) {
            return false;
        }

        String token = query.substring("token=".length());
        log.info("token: {}", token);

        String key = "ws:temp:claim:" + token;
        Map<String, Object> data = (Map<String, Object>) redisTemplate.opsForValue().get(key);

        attributes.put("memberId", data.get("memberId"));
        attributes.put("email", data.get("memberEmail"));
        attributes.put("userId", data.get("userId"));

        log.info("attributes {}", attributes);


//        ContextHolder 사용 시 로직 ws연결 시 쿠키를 얻어오지 못해(시큐리티 필터에서 Anonymous처리 되어) 주석처리

//        // 인증 실패 시 false 반환 핸드셰이크 실패처리
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        log.info("authentication: {}", authentication);
//        // 인증이 없거나 인증되지 않은 경우
//        if (authentication == null || !authentication.isAuthenticated()) {
//            log.warn("authentication is not authenticated");
//            return false;
//        }
//
//        // principal이 CustomMemberDetails 타입이 아닌 경우
//        Object principal = authentication.getPrincipal();
//        if (!(principal instanceof CustomMemberDetails)) {
//            log.warn("principal is not valid");
//            return false;
//        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
        Exception exception) {

    }
}
