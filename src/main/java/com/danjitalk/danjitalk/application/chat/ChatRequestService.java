package com.danjitalk.danjitalk.application.chat;

import com.danjitalk.danjitalk.common.exception.BadRequestException;
import com.danjitalk.danjitalk.common.exception.ConflictException;
import com.danjitalk.danjitalk.common.exception.DataNotFoundException;
import com.danjitalk.danjitalk.common.exception.ForbiddenException;
import com.danjitalk.danjitalk.common.util.SecurityContextHolderUtil;
import com.danjitalk.danjitalk.domain.chat.dto.ChatRequestResponse;
import com.danjitalk.danjitalk.domain.chat.dto.CreateChatRequest;
import com.danjitalk.danjitalk.domain.chat.dto.MemberInformation;
import com.danjitalk.danjitalk.domain.chat.entity.ChatRequest;
import com.danjitalk.danjitalk.domain.chat.entity.Chatroom;
import com.danjitalk.danjitalk.domain.chat.entity.ChatroomMemberMapping;
import com.danjitalk.danjitalk.domain.chat.enums.ChatRequestStatus;
import com.danjitalk.danjitalk.domain.chat.enums.ChatroomType;
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
                .message(request.message())
                .build();

        chatRequestRepository.save(chatRequest);
    }

    /**
     * 채팅 요청 승인
     * @param requestId
     */
    @Transactional
    public void approveRequest(Long requestId) {
        Long currentId = SecurityContextHolderUtil.getMemberId();
        ChatRequest chatRequest = chatRequestRepository.findChatRequestWithRequesterAndReceiverById(requestId).orElseThrow(DataNotFoundException::new);

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
                .type(ChatroomType.ONE_ON_ONE)
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

    /**
     * 채팅 요청 거절
     * @param requestId
     */
    @Transactional
    public void rejectRequest(Long requestId) {
        Long currentId = SecurityContextHolderUtil.getMemberId();
        ChatRequest chatRequest = chatRequestRepository.findById(requestId).orElseThrow(DataNotFoundException::new);

        if (chatRequest.getStatus() != ChatRequestStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 요청");
        }

        Long receiverId = chatRequest.getReceiver().getId();

        if (!currentId.equals(receiverId)) {
            throw new IllegalStateException("권한이 없습니다!");
        }

        // 거절 처리
        chatRequest.changeStatus(ChatRequestStatus.APPROVED);
        chatRequestRepository.save(chatRequest);
    }

    /**
     * 받은 채팅 요청 리스트
     * @return
     */
    public List<ChatRequestResponse> receivedRequests() {
        Long currentId = SecurityContextHolderUtil.getMemberId();
        return chatRequestRepository.findChatRequestWithRequesterByReceiverId(currentId)
                .stream()
                .map(chatRequest ->
                    new ChatRequestResponse(
                        chatRequest.getMessage(),
                        MemberInformation.from(chatRequest.getRequester()),
                        chatRequest.getId(),
                        chatRequest.getStatus(),
                        chatRequest.getCreatedAt()
                    )
                ).toList();
    }

    /**
     * 보낸 채팅 요청 리스트
     * @return
     */
    public List<ChatRequestResponse> sentRequests() {
        Long currentId = SecurityContextHolderUtil.getMemberId();
        return chatRequestRepository.findChatRequestWithRequesterByRequesterId(currentId)
                .stream()
                .map(chatRequest ->
                    new ChatRequestResponse(
                        chatRequest.getMessage(),
                        MemberInformation.from(chatRequest.getRequester()),
                        chatRequest.getId(),
                        chatRequest.getStatus(),
                        chatRequest.getCreatedAt()
                    )
                ).toList();
    }

    /**
     * 보낸 채팅 요청 삭제
     */
    @Transactional
    public void cancelRequest(Long requestId) {
        Long currentId = SecurityContextHolderUtil.getMemberId();
        ChatRequest chatRequest = chatRequestRepository.findById(requestId).orElseThrow(DataNotFoundException::new);

        if (!chatRequest.getRequester().getId().equals(currentId)) {
            throw new ForbiddenException(403, "내가 보낸 요청이 아닙니다.");
        }

        if (chatRequest.getStatus() == ChatRequestStatus.APPROVED) {
            throw new BadRequestException("이미 수락된 요청은 취소할 수 없습니다.");
        }

        chatRequestRepository.delete(chatRequest);
    }
}
