package com.danjitalk.danjitalk.application.chat;

import com.danjitalk.danjitalk.domain.chat.entity.Chatroom;
import com.danjitalk.danjitalk.domain.chat.entity.ChatroomMemberMapping;
import com.danjitalk.danjitalk.domain.user.member.entity.Member;
import com.danjitalk.danjitalk.infrastructure.repository.chat.ChatroomMemberMappingRepository;
import com.danjitalk.danjitalk.infrastructure.repository.chat.ChatroomRepository;
import com.danjitalk.danjitalk.infrastructure.repository.user.member.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatroomRepository chatroomRepository;
    private final ChatroomMemberMappingRepository chatroomMemberMappingRepository;
    private final MemberRepository memberRepository;

    /**
     * 채팅방 생성 메소드
     * @return 채팅방
     */
    public Chatroom createChatroom(String chatroomName) {
        Chatroom chatroom = Chatroom.builder()
                .isPrivate(false) // TODO: 채팅방 암호화 기능
                .name(chatroomName)
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
     * @param memberId
     * @param chatroomId
     */
    public void joinChatroom(Long memberId, Long chatroomId) {
        log.info("memberId={}, chatroomId={}", memberId, chatroomId);
        Chatroom chatroom = chatroomRepository.findById(chatroomId).orElseThrow();
        Member member = memberRepository.findById(memberId).orElseThrow();

        ChatroomMemberMapping chatroomMemberMapping = ChatroomMemberMapping.builder()
                .chatroom(chatroom)
                .member(member)
                .chatroomName(chatroom.getName())
                .build();

        chatroomMemberMappingRepository.save(chatroomMemberMapping);
    }

}
