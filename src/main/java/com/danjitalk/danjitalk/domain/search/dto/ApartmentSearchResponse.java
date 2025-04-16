package com.danjitalk.danjitalk.domain.search.dto;

public record ApartmentSearchResponse(
    String name,
    String region,
    String location,
    Integer totalUnit,
    Integer buildingCount,
    String thumbnailFileUrl,
    Boolean isBookmarked
) {

}
