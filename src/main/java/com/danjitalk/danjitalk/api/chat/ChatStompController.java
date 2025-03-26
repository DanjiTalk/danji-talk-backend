package com.danjitalk.danjitalk.api.chat;

import com.danjitalk.danjitalk.application.chat.ChatService;
import com.danjitalk.danjitalk.common.security.CustomMemberDetails;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatStompController {

    private final ChatService chatService;

    @SubscribeMapping("/subscribe")  //  /subscribe
    public List<Long> getSubscribes(SimpMessageHeaderAccessor accessor) {
        Principal principal = accessor.getUser();
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        CustomMemberDetails customMemberDetails = (CustomMemberDetails) token.getPrincipal();
        Long memberId = customMemberDetails.getUser().getMember().getId();
        return chatService.subscribeRoomIds(memberId);
    }

    @SubscribeMapping("/topic/chat/{roomId}") // /topic/chat/7
    public void joinChatroom(
        @DestinationVariable Long roomId,
        SimpMessageHeaderAccessor accessor
    ) {
        Principal principal = accessor.getUser();
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        CustomMemberDetails customMemberDetails = (CustomMemberDetails) token.getPrincipal();
        Long memberId = customMemberDetails.getUser().getMember().getId();
        chatService.joinChatroom(memberId, roomId);
    }
}
