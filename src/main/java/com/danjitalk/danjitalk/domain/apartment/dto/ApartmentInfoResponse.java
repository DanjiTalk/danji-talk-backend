package com.danjitalk.danjitalk.domain.apartment.dto;

import com.danjitalk.danjitalk.domain.apartment.entity.Apartment;

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
    public static ApartmentInfoResponse from(Apartment apartment){
        return new ApartmentInfoResponse(
            apartment.getName(),
            apartment.getRegion(),
            apartment.getLocation(),
            apartment.getTotalUnit(),
            apartment.getParkingCapacity(),
            apartment.getBuildingCount(),
            apartment.getBuildingRange(),
            apartment.getFileUrl(),
            apartment.getChatroomId()
        );
    }

}
