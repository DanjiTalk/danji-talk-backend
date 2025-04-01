package com.danjitalk.danjitalk.api.chat;

import com.danjitalk.danjitalk.application.chat.ChatService;
import com.danjitalk.danjitalk.common.response.ApiResponse;
import com.danjitalk.danjitalk.domain.chat.dto.ChatroomSummaryResponse;
import com.danjitalk.danjitalk.domain.chat.enums.ChatroomType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/direct")
    public ResponseEntity<ApiResponse<List<ChatroomSummaryResponse>>> getDirectChats() {
        List<ChatroomSummaryResponse> directChatList = chatService.getChatListByType(ChatroomType.ONE_ON_ONE);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), null, directChatList));
    }

    @GetMapping("/group")
    public ResponseEntity<ApiResponse<List<ChatroomSummaryResponse>>> getGroupChats() {
        List<ChatroomSummaryResponse> groupChatList = chatService.getChatListByType(ChatroomType.GROUP);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), null, groupChatList));
    }
}
