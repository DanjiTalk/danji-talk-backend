package com.danjitalk.danjitalk.event.handler;

import com.danjitalk.danjitalk.application.chat.ChatService;
import com.danjitalk.danjitalk.common.security.CustomMemberDetails;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class StompEventHandler { //StompSubProtocolHandler 에서 이벤트 처리

    private final ChatService chatService;

    @EventListener
    public void handleSubscribeListener(SessionSubscribeEvent event) { // StompCommand.SUBSCRIBE 일 때 실행
        log.info("사용자 구독 시(채팅방 접속)");
    }

    @EventListener
    public void handleUnsubscribeListener(SessionUnsubscribeEvent event) { // StompCommand.UNSUBSCRIBE 일 때 실행
        log.info("사용자 구독 취소 시");
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String destination = accessor.getDestination();
        Assert.notNull(destination, "destination은 null일 수 없습니다.");

        Long roomId = Long.parseLong(destination.substring(destination.lastIndexOf("/") + 1));
        Long memberId = (Long) accessor.getSessionAttributes().get("memberId");

        chatService.exitRoom(roomId, memberId);
    }

    @EventListener
    public void handleConnectEventListener(SessionConnectEvent event) { // StompCommand.CONNECT or StompCommand.STOMP 일 때 실행
        log.info("사용자 연결 전 소켓 연결, 헤더에 기본값 세팅");
//        기본값 세팅은 핸드셰이크에서 하는게 맞으나 참고용으로 남김
//        MessageHeaders headers = event.getMessage().getHeaders();
//        log.info("headers {}", headers);
//
//        Map<String, Object> sessionAttributes = (Map<String, Object>) headers.get("simpSessionAttributes");
//        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) headers.get("simpUser");
//        String email = token.getName();
//        CustomMemberDetails customMemberDetails = (CustomMemberDetails) token.getPrincipal();
//        sessionAttributes.put("email", email);
//        sessionAttributes.put("memberId", customMemberDetails.getUser().getMember().getId());
//
//        log.info("After setting the headers: {}", headers);
    }

    @EventListener
    public void handleConnectedListener(SessionConnectedEvent event) { // StompCommand.CONNECTED 일 때 실행, 연결 후 응답 하느라고 accessor(message) 값이 달라짐
        log.info("사용자 연결 후");
    }

    @EventListener
    public void handleDisconnectionListener(SessionDisconnectEvent event) { // afterSessionEnded 호출 시
        log.info("사용자 연결종료");
    }
}
