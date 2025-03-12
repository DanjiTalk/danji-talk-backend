package com.danjitalk.danjitalk.domain.apartment.dto;

import lombok.Builder;

@Builder
public record ApartmentRegisterResponse(
    Long id,
    String name,
    String region,
    String location,
    Integer totalUnit,
    Integer parkingCapacity,
    Integer buildingCount,
    String fileUrl
) {

}
