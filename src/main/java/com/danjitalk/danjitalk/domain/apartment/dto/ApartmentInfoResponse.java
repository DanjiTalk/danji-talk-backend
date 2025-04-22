package com.danjitalk.danjitalk.domain.apartment.dto;

import lombok.Builder;

@Builder
public record ApartmentInfoResponse(
    String name,
    String region,
    String location,
    Integer totalUnit,
    Integer parkingCapacity,
    Integer buildingCount,
    String buildingRange,
    String fileUrl,
    Long chatroomId
) {
}
