package com.danjitalk.danjitalk.application.chat;

import com.danjitalk.danjitalk.common.exception.BadRequestException;
import com.danjitalk.danjitalk.common.exception.ConflictException;
import com.danjitalk.danjitalk.common.exception.DataNotFoundException;
import com.danjitalk.danjitalk.common.util.SecurityContextHolderUtil;
import com.danjitalk.danjitalk.domain.chat.dto.ApproveChatRequest;
import com.danjitalk.danjitalk.domain.chat.dto.CreateChatRequest;
import com.danjitalk.danjitalk.domain.chat.entity.ChatRequest;
import com.danjitalk.danjitalk.domain.chat.entity.Chatroom;
import com.danjitalk.danjitalk.domain.chat.entity.ChatroomMemberMapping;
import com.danjitalk.danjitalk.domain.chat.enums.ChatRequestStatus;
import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import com.danjitalk.danjitalk.infrastructure.repository.chat.ChatRequestRepository;
import com.danjitalk.danjitalk.infrastructure.repository.chat.ChatroomMemberMappingRepository;
import com.danjitalk.danjitalk.infrastructure.repository.chat.ChatroomRepository;
import com.danjitalk.danjitalk.infrastructure.repository.user.member.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRequestService {

    private final ChatRequestRepository chatRequestRepository;
    private final MemberRepository memberRepository;
    private final ChatroomRepository chatroomRepository;
    private final ChatroomMemberMappingRepository chatroomMemberMappingRepository;

    /**
     * 채팅 요청
     * @param request
     */
    @Transactional
    public void requestChat(CreateChatRequest request) {
        Long currentId = SecurityContextHolderUtil.getMemberId();

        if (currentId.equals(request.receiverId())) {
            throw new BadRequestException("본인에게 보낼 수 없습니다.");
        }

        // 이미 보낸 신청이 있는지 확인 (중복 방지)
        boolean exists = chatRequestRepository.existsByRequesterIdAndReceiverIdAndStatus(currentId, request.receiverId(), ChatRequestStatus.PENDING);

        if (exists) {
            throw new ConflictException("이미 채팅 신청을 보냈습니다.");
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

    /**
     * 채팅 요청 승인
     * @param request
     */
    @Transactional
    public void approveRequest(ApproveChatRequest request) {
        Long currentId = SecurityContextHolderUtil.getMemberId();
        ChatRequest chatRequest = chatRequestRepository.findChatRequestWithRequesterAndReceiverById(request.requestId()).orElseThrow(DataNotFoundException::new);

        if (chatRequest.getStatus() != ChatRequestStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 요청");
        }

        Member requester = chatRequest.getRequester();
        Member receiver = chatRequest.getReceiver();

        if (!currentId.equals(receiver.getId())) {
            throw new IllegalStateException("권한이 없습니다!");
        }

        // 승인 처리
        chatRequest.changeStatus(ChatRequestStatus.APPROVED);

        // 채팅방 생성
        Chatroom chatroom = Chatroom.builder()
                .name(requester.getNickname() + "님이 신청한 대화")
                .isPrivate(true) // 1대1 채팅
                .build();

        Long chatroomId = chatroomRepository.save(chatroom).getId();
        chatRequest.initRoom(chatroomId);

        chatRequestRepository.save(chatRequest);

        // 맴버 매핑 requester
        List<ChatroomMemberMapping> mappings = List.of(
            ChatroomMemberMapping.builder()
                .chatroom(chatroom)
                .chatroomName(receiver.getNickname())
                .member(requester)
                .build(),
            ChatroomMemberMapping.builder()
                .chatroom(chatroom)
                .chatroomName(requester.getNickname())
                .member(receiver)
                .build()
        );

        chatroomMemberMappingRepository.saveAll(mappings);
    }
}
