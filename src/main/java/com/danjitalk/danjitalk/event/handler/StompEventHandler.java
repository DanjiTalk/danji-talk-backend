package com.danjitalk.danjitalk.event.handler;

import com.danjitalk.danjitalk.common.security.CustomMemberDetails;
import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import com.danjitalk.danjitalk.infrastructure.repository.user.member.MemberRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class StompEventHandler { //StompSubProtocolHandler 에서 이벤트 처리

    private final SimpMessagingTemplate messagingTemplate; // 메시지를 보낼 수 있는 템플릿
    private final MemberRepository memberRepository;

    @EventListener
    public void handleSubscribeListener(SessionSubscribeEvent event) { // StompCommand.SUBSCRIBE 일 때 실행
        log.info("사용자 구독 시(채팅방 접속)");

        MessageHeaders headers = event.getMessage().getHeaders();
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) headers.get("simpUser");
        String email = token.getName(); // loginId -> email
        Member member = memberRepository.findByEmail(email).orElseThrow();
        String destination = (String) headers.get("simpDestination");
        log.info("destination: {}, headers: {}", destination, headers);

        if (destination != null && destination.startsWith("/topic/chat/")) {
            messagingTemplate.convertAndSend(destination, member.getNickname() + " 사용자가 입장했습니다.");
        }
    }

    @EventListener
    public void handleUnsubscribeListener(SessionUnsubscribeEvent event) { // StompCommand.UNSUBSCRIBE 일 때 실행
        log.info("사용자 구독 취소 시");
    }

    @EventListener
    public void handleConnectEventListener(SessionConnectEvent event) { // StompCommand.CONNECT or StompCommand.STOMP 일 때 실행
        log.info("사용자 연결 전 소켓 연결, 헤더에 기본값 세팅");

        MessageHeaders headers = event.getMessage().getHeaders();
        log.info("headers {}", headers);

        Map<String, Object> sessionAttributes = (Map<String, Object>) headers.get("simpSessionAttributes");
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) headers.get("simpUser");
        String email = token.getName();
        CustomMemberDetails customMemberDetails = (CustomMemberDetails) token.getPrincipal();
        sessionAttributes.put("email", email);
        sessionAttributes.put("memberId", customMemberDetails.getUser().getMember().getId());

        log.info("After setting the headers: {}", headers);
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
