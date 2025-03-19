package com.danjitalk.danjitalk.application.chat;

import com.danjitalk.danjitalk.domain.chat.entity.Chatroom;
import com.danjitalk.danjitalk.infrastructure.repository.chat.ChatroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatroomRepository chatroomRepository;

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
}
