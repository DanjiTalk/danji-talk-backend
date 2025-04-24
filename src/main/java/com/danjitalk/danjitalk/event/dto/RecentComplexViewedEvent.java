package com.danjitalk.danjitalk.event.dto;

public record RecentComplexViewedEvent(
    Long id,
    String name,
    String region,
    String location,
    Integer totalUnit,
    Integer buildingCount,
    String thumbnailFileUrl,
    Long memberId
) {

}
