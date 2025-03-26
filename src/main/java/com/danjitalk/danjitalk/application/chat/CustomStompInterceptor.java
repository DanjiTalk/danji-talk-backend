package com.danjitalk.danjitalk.application.chat;

import com.danjitalk.danjitalk.common.security.CustomMemberDetails;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomStompInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) { // 연결 접속 메시지 등 통신 할 때마다...
        log.info("--------------------Stomp Handler 실행--------------------");
        MessageHeaders headers = message.getHeaders();
        log.info("headers {}", headers);

        if (headers.get("stompCommand") == StompCommand.CONNECT) { // 핸드셰이크 이후 연결 세션에 초기값 세팅
            log.info("소켓 연결, 헤더에 기본값 세팅");
            Map<String, Object> sessionAttributes = (Map<String, Object>) headers.get("simpSessionAttributes");
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) headers.get("simpUser");
            String email = token.getName();
            CustomMemberDetails customMemberDetails = (CustomMemberDetails) token.getPrincipal();
            sessionAttributes.put("email", email);
            sessionAttributes.put("memberId", customMemberDetails.getUser().getMember().getId());
        }
        log.info("After setting the headers: {}", headers);

        log.info("--------------------Stomp Handler 종료--------------------");
        return message;
    }
}



