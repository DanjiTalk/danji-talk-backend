package com.danjitalk.danjitalk.domain.apartment.dto;

public record RecentComplexViewedDTO(
    Long id,
    String name,
    String region,
    String location,
    Integer totalUnit,
    Integer buildingCount,
    String thumbnailFileUrl
) {

}
