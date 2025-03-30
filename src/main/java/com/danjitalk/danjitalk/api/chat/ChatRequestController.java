package com.danjitalk.danjitalk.api.chat;

import com.danjitalk.danjitalk.application.chat.ChatRequestService;
import com.danjitalk.danjitalk.common.response.ApiResponse;
import com.danjitalk.danjitalk.domain.chat.dto.CreateChatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/request")
public class ChatRequestController {

    private final ChatRequestService chatRequestService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> requestChat(@RequestBody CreateChatRequest request) {
        chatRequestService.requestChat(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{requestId}/approve")
    public ResponseEntity<ApiResponse<Void>> approveChat(@PathVariable Long requestId) {
        chatRequestService.approveRequest(requestId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{requestId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectChatRequest(@PathVariable Long requestId) {
        chatRequestService.rejectRequest(requestId);
        return ResponseEntity.ok().build();
    }

}
