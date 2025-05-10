package com.danjitalk.danjitalk.domain.user.member.dto.response;

public record MypageResponse(
    String fileId,
    String name,
    String nickname,
    String email,

    Long memberApartmentId,
    String apartmentName,
    String region,
    String location,
    String building,
    String unit
) {

}
