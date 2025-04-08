package com.danjitalk.danjitalk.infrastructure.repository.chat;

import com.danjitalk.danjitalk.domain.chat.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

}
