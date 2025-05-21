package com.danjitalk.danjitalk.domain.apartment.dto;

import com.danjitalk.danjitalk.domain.apartment.entity.Apartment;
import com.danjitalk.danjitalk.event.dto.RecentComplexViewedEvent;

public record ApartmentCache(
    Long id,
    String name,
    String region,
    String location,
    Integer totalUnit,
    Integer buildingCount,
    String thumbnailFileUrl,
    Integer parkingCapacity,
    String buildingRange,
    String fileUrl,
    Long chatroomId
) {
    public ApartmentInfoResponse toResponse() {
        return new ApartmentInfoResponse(
            name, region, location, totalUnit, parkingCapacity,
            buildingCount, buildingRange, fileUrl, chatroomId
        );
    }

    public RecentComplexViewedEvent toEvent(Long memberId) {
        return new RecentComplexViewedEvent(
            id, name, region, location, totalUnit, buildingCount, thumbnailFileUrl, memberId
        );
    }

    public static ApartmentCache from(Apartment apartment){
        return new ApartmentCache(
            apartment.getId(),
            apartment.getName(),
            apartment.getRegion(),
            apartment.getLocation(),
            apartment.getTotalUnit(),
            apartment.getBuildingCount(),
            apartment.getThumbnailFileUrl(),
            apartment.getParkingCapacity(),
            apartment.getBuildingRange(),
            apartment.getFileUrl(),
            apartment.getChatroomId()
        );
    }
}
