package com.danjitalk.danjitalk.event.handler;

import com.danjitalk.danjitalk.application.chat.ChatService;
import com.danjitalk.danjitalk.domain.apartment.entity.Apartment;
import com.danjitalk.danjitalk.domain.chat.entity.Chatroom;
import com.danjitalk.danjitalk.event.dto.GroupChatCreateEvent;
import com.danjitalk.danjitalk.infrastructure.repository.apartment.ApartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventHandler {

    private final ChatService chatService;
    private final ApartmentRepository apartmentRepository;

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleGroupChatCreateEvent(GroupChatCreateEvent event) {
        Chatroom chatroom = chatService.createChatroom(event.groupChatName());
        Apartment apartment = apartmentRepository.findByName(event.groupChatName()).orElseThrow();
        apartment.addChatroom(chatroom);
        apartmentRepository.save(apartment);
    }
}
