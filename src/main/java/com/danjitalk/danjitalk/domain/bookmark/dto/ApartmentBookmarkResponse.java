package com.danjitalk.danjitalk.domain.bookmark.dto;

public record ApartmentBookmarkResponse(
    Long bookmarkId,

    Long apartmentId,
    String name,
    String region,
    String location,
    Integer totalUnit,
    Integer buildingCount,
    String thumbnailFileUrl
) implements IBookmarkResponse {

}