package com.danjitalk.danjitalk.domain.chat.dto;

import java.util.List;

public record ChatroomDetailsResponse(
    String chatroomName,
    int memberCount,
    List<ChatMessageResponse> chatMessageResponses,
    List<MemberInformation> memberInformationList
) {

}
