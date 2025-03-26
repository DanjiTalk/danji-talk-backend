package com.danjitalk.danjitalk.application.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomStompInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) { // 연결 접속 메시지 등 통신 할 때마다...
        log.info("--------------------Stomp Handler 실행--------------------");
        log.info("--------------------Stomp Handler 종료--------------------");
        return message;
    }
}



