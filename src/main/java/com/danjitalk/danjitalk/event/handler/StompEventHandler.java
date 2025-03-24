package com.danjitalk.danjitalk.event.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
@Slf4j
public class StompEventHandler {

    @EventListener
    public void handleSubscribeListener(SessionSubscribeEvent event){
        log.info("사용자 구독 시(채팅방 접속)");
    }

    @EventListener
    public void handleUnsubscribeListener(SessionUnsubscribeEvent event){
        log.info("사용자 구독 취소 시");

    }

    @EventListener
    public void handleConnectEventListener(SessionConnectEvent event){
        log.info("사용자 연결 전");
    }

    @EventListener
    public void handleConnectedListener(SessionConnectedEvent event){
        log.info("사용자 연결 후");
    }

    @EventListener
    public void handleDisconnectionListener(SessionDisconnectEvent event){
        log.info("사용자 연결종료");
    }
}
