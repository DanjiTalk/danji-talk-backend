package com.danjitalk.danjitalk.api.chat;

import com.danjitalk.danjitalk.application.chat.ChatService;
import com.danjitalk.danjitalk.domain.chat.dto.ChatMessageResponse;
import com.danjitalk.danjitalk.domain.chat.dto.ChatMessageRequest;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatStompController {

    private final ChatService chatService;

    @SubscribeMapping("/subscribe")  //  /subscribe
    public List<Long> getSubscribes(StompHeaderAccessor accessor) {
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

        if (sessionAttributes == null || !sessionAttributes.containsKey("memberId")) {
            throw new IllegalStateException("WebSocket session does not contain memberId.");
        }

        Long memberId = (Long) sessionAttributes.get("memberId");
        return chatService.subscribeRoomIds(memberId);
    }

    //Todo: StompHeaderAccessor accessor는 컨트롤러단에서 다루는것이 좋다.
    @SubscribeMapping("/topic/chat/{roomId}") // /topic/chat/7
    public void joinChatroom(
        @DestinationVariable Long roomId,
        StompHeaderAccessor accessor
    ) {
        chatService.joinChatroom(roomId, accessor);
    }

    /**
     * 채팅 메시지 작성
     * @param roomId
     * @param request
     * @param accessor
     * @return
     */
    // return + @SendTo or SimpMessagingTemplate.convertAndSend
    @MessageMapping("/chat/{roomId}")  // request: /pub/chat/{roomId}
    @SendTo("/topic/chat/{roomId}")  // subscribe 채팅방으로 메세지 전송
    public ChatMessageResponse createChatMessage(
        @DestinationVariable Long roomId,
        @Payload ChatMessageRequest request,
        StompHeaderAccessor accessor
    ) {
        log.info("accessor {}", accessor);
        String email = (String) accessor.getSessionAttributes().get("email");
        log.info("roomId {}, email: {}", roomId, email);
        return chatService.createChatMessage(request, roomId, email);
    }
}
