package com.danjitalk.danjitalk.domain.user.member.dto.response;

public record MyPageResponse(
    String fileId,
    String name,
    String nickname,
    String email,

    Long memberApartmentId,
    Long apartmentId,
    String apartmentName,
    String region,
    String location,
    String building,
    String unit
) {

}
