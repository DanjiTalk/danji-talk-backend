package com.danjitalk.danjitalk.application.chat;

import com.danjitalk.danjitalk.common.util.SecurityContextHolderUtil;
import com.danjitalk.danjitalk.domain.chat.dto.CreateChatRequest;
import com.danjitalk.danjitalk.domain.chat.entity.ChatRequest;
import com.danjitalk.danjitalk.domain.chat.enums.ChatRequestStatus;
import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import com.danjitalk.danjitalk.infrastructure.repository.chat.ChatRequestRepository;
import com.danjitalk.danjitalk.infrastructure.repository.user.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRequestService {

    private final ChatRequestRepository chatRequestRepository;
    private final MemberRepository memberRepository;

    /**
     * 채팅 요청
     * @param request
     */
    @Transactional
    public void requestChat(CreateChatRequest request) {
        Long currentId = SecurityContextHolderUtil.getMemberId();

        // 이미 보낸 신청이 있는지 확인 (중복 방지)
        boolean exists = chatRequestRepository.existsByRequesterIdAndReceiverIdAndStatus(currentId, request.receiverId(), ChatRequestStatus.PENDING);

        if (exists) {
            throw new IllegalStateException("이미 채팅 신청을 보냈습니다.");
        }

        Member requester = memberRepository.findById(currentId).orElseThrow();
        Member receiver = memberRepository.findById(request.receiverId()).orElseThrow();

        // 새로운 채팅 신청 생성
        ChatRequest chatRequest = ChatRequest.builder()
                .requester(requester)  // 요청자
                .receiver(receiver)  // 받는 사람
                .status(ChatRequestStatus.PENDING)  // 대기 상태
                .build();

        chatRequestRepository.save(chatRequest);
    }
}
