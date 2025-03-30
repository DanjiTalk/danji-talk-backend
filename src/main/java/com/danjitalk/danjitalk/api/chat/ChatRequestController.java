package com.danjitalk.danjitalk.api.chat;

import com.danjitalk.danjitalk.application.chat.ChatRequestService;
import com.danjitalk.danjitalk.common.response.ApiResponse;
import com.danjitalk.danjitalk.domain.chat.dto.DecisionChatRequest;
import com.danjitalk.danjitalk.domain.chat.dto.CreateChatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRequestController {

    private final ChatRequestService chatRequestService;

    @PostMapping("/request")
    public ResponseEntity<ApiResponse<Void>> requestChat(@RequestBody CreateChatRequest request) {
        chatRequestService.requestChat(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/approve")
    public ResponseEntity<ApiResponse<Void>> approveChat(@RequestBody DecisionChatRequest request) {
        chatRequestService.approveRequest(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject")
    public ResponseEntity<ApiResponse<Void>> rejectChatRequest(@RequestBody DecisionChatRequest request) {
        chatRequestService.rejectRequest(request);
        return ResponseEntity.ok().build();
    }

}
