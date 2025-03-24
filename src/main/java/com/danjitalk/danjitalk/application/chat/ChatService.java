package com.danjitalk.danjitalk.application.chat;

import com.danjitalk.danjitalk.domain.chat.entity.Chatroom;
import com.danjitalk.danjitalk.domain.chat.entity.ChatroomMemberMapping;
import com.danjitalk.danjitalk.infrastructure.repository.chat.ChatroomMemberMappingRepository;
import com.danjitalk.danjitalk.infrastructure.repository.chat.ChatroomRepository;
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

}
