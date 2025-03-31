package com.danjitalk.danjitalk.application.chat;

import com.danjitalk.danjitalk.common.exception.DataNotFoundException;
import com.danjitalk.danjitalk.common.util.SecurityContextHolderUtil;
import com.danjitalk.danjitalk.domain.chat.dto.ChatMessageRequest;
import com.danjitalk.danjitalk.domain.chat.dto.ChatMessageResponse;
import com.danjitalk.danjitalk.domain.chat.dto.MemberInformation;
import com.danjitalk.danjitalk.domain.chat.dto.DirectChatResponse;
import com.danjitalk.danjitalk.domain.chat.entity.ChatMessage;
import com.danjitalk.danjitalk.domain.chat.entity.Chatroom;
import com.danjitalk.danjitalk.domain.chat.entity.ChatroomMemberMapping;
import com.danjitalk.danjitalk.domain.chat.enums.ChatroomType;
import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import com.danjitalk.danjitalk.infrastructure.mongo.chat.ChatMessageMongoRepository;
import com.danjitalk.danjitalk.infrastructure.repository.chat.ChatroomMemberMappingRepository;
import com.danjitalk.danjitalk.infrastructure.repository.chat.ChatroomRepository;
import com.danjitalk.danjitalk.infrastructure.repository.user.member.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatroomRepository chatroomRepository;
    private final ChatroomMemberMappingRepository chatroomMemberMappingRepository;
    private final MemberRepository memberRepository;
    private final ChatMessageMongoRepository chatMessageMongoRepository;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 채팅방 생성 메소드
     * @return 채팅방
     */
    public Chatroom createChatroom(String chatroomName) {
        Chatroom chatroom = Chatroom.builder()
                .isPrivate(false) // TODO: 채팅방 암호화 기능
                .name(chatroomName)
                .type(ChatroomType.GROUP)
                .build();
        return chatroomRepository.save(chatroom);
    }

    /**
     * 구독 방 번호 조회
     * @param memberId
     * @return 구독 할 번호
     */
    public List<Long> subscribeRoomIds(Long memberId) {
        List<ChatroomMemberMapping> chatroomMemberMappings = chatroomMemberMappingRepository.findChatroomMemberMappingByMemberId(memberId);
        return chatroomMemberMappings.stream()
            .map(e -> e.getChatroom().getId())
            .toList();
    }

    /**
     * 채팅방 참여
     * @param chatroomId 방 번호
     * @param accessor StompHeader
     */
    @Transactional
    public void joinChatroom(Long chatroomId, StompHeaderAccessor accessor) {
        String email = (String) accessor.getSessionAttributes().get("email");
        String destination = accessor.getDestination();
        Long memberId = (Long) accessor.getSessionAttributes().get("memberId");
        log.info("memberId={}, chatroomId={}, destination: {}", memberId, chatroomId, destination);

        Member member = memberRepository.findByEmail(email).orElseThrow();
        Chatroom chatroom = chatroomRepository.findById(chatroomId).orElseThrow();

        boolean isAlreadyInChatroom = chatroomMemberMappingRepository.existsByChatroomAndMember(chatroom, member);
        log.info("isAlreadyInChatroom: {}", isAlreadyInChatroom);
        if(!isAlreadyInChatroom) {
            ChatroomMemberMapping chatroomMemberMapping = ChatroomMemberMapping.builder()
                    .chatroom(chatroom)
                    .member(member)
                    .chatroomName(chatroom.getName())
                    .build();

            chatroomMemberMappingRepository.save(chatroomMemberMapping);

            if (destination != null && destination.startsWith("/topic/chat/")) {
                messagingTemplate.convertAndSend(destination, member.getNickname() + " 사용자가 입장했습니다.");
            }
        }
    }

    /**
     *  채팅 메시지 전송
     * @param request 메시지 내용
     * @param roomId 채팅방 번호
     * @param email 보내는 사람 이메일
     * @return
     */
    @Transactional
    public ChatMessageResponse createChatMessage(ChatMessageRequest request, Long roomId, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(DataNotFoundException::new);
        Chatroom chatRoom = chatroomRepository.findById(roomId).orElseThrow(DataNotFoundException::new);

        // 메시지 저장
        ChatMessage message = saveMessage(chatRoom, member, request);

        // 회원 정보
        MemberInformation memberInformation = MemberInformation.from(member);

        return ChatMessageResponse.builder()
                .id(message.getId())
                .chatroomId(chatRoom.getId())
                .sender(memberInformation)
                .message(message.getMessage())
                .imageUrl(message.getImageUrl())
                .createdAt(message.getCreatedAt())
                .build();
    }

    private ChatMessage saveMessage(Chatroom chatRoom, Member member, ChatMessageRequest request) {
        ChatMessage chatMessage = ChatMessage.builder()
                .chatroomId(chatRoom.getId())
                .senderId(member.getId())
                .message(request.message())
                .imageUrl("")
                .build();

        return chatMessageMongoRepository.save(chatMessage);
    }

    /**
     * 1대1 채팅 목록 조회
     */
    @Transactional
    public List<DirectChatResponse> getDirectChatList() {
        Long currentId = SecurityContextHolderUtil.getMemberId();
        return chatroomMemberMappingRepository.findChatroomMemberMappingWithMemberAndChatroomByMemberId(currentId)
                .stream()
                .map(chatroomMemberMapping -> {
                    Long roomId = chatroomMemberMapping.getChatroom().getId();
                    return chatMessageMongoRepository.findTopByChatroomIdOrderByCreatedAtDesc(roomId)
                        .map(chatMessage ->
                            new DirectChatResponse(
                                roomId,
                                MemberInformation.from(chatroomMemberMapping.getMember()),
                                chatMessage.getMessage(),
                                chatMessage.getCreatedAt()
                            )
                        ).orElseGet(
                            () -> new DirectChatResponse(
                                roomId,
                                MemberInformation.from(chatroomMemberMapping.getMember()),
                                "대화를 시작해보세요",
                                chatroomMemberMapping.getChatroom().getCreatedAt()
                            )
                        );
                }).toList();
    }
}
