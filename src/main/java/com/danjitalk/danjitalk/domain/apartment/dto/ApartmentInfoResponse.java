package com.danjitalk.danjitalk.domain.apartment.dto;

import com.danjitalk.danjitalk.domain.apartment.dto.openapi.apartment.Body;
import com.danjitalk.danjitalk.domain.apartment.dto.openapi.apartment.basic.ApartmentBasicInfo;
import com.danjitalk.danjitalk.domain.apartment.dto.openapi.apartment.basic.BasicItem;
import com.danjitalk.danjitalk.domain.apartment.dto.openapi.apartment.detail.ApartmentDetailInfo;
import com.danjitalk.danjitalk.domain.apartment.dto.openapi.apartment.detail.DetailItem;
import com.danjitalk.danjitalk.domain.apartment.entity.Apartment;
import lombok.Builder;

@Builder
public record ApartmentInfoResponse(
    String kaptCode,
    String kaptName,
    String kaptUsedate,     // 사용 승인일
    Integer kaptdaCnt,   // 세대 수
    String kaptDongCnt,     // 동 수
    String codeAptNm,       // 건물 유형
    Integer kaptTopFloor,   // 최고 층
    Integer kaptMparea60,   // 60㎡ 이하
    Integer kaptMparea85,   // 85㎡ 이하
    Integer kaptMparea135,  // 135㎡ 이하
    Integer kaptMparea136,  // 136㎡ 이상
    String kaptTel,         // 관리사무소 전화
    String kaptdPcnt,        // 주차대수 지상
    String kaptdPcntu,       //  주차대수 지하
    String codeHeatNm,     // 난방 방식
    String kaptdWtimebus,   // 버스정류장 접근성
    String subwayLine,       // 지하철 노선
    String subwayStation,    // 지하철역
    String kaptdWtimesub,    // 지하철 접근 시간
    String convenientFacility, // 편의시설
    String educationFacility,  // 교육시설
    Integer groundElChargerCnt,     // 지상 전기차 충전기
    Integer undergroundElChargerCnt, // 지하 전기차 충전기
    String welfareFacility,     // 단지내 시설 복지시설
    String kaptdCccnt,         // CCTV 대수

    String name, // 이름
    String region, // 지역
    String location, // 지역?
    Integer totalUnit, // 동 수
    Integer parkingCapacity, // 주차공간
    Integer buildingCount, // 동 수
    String buildingRange, // 빌딩 범위  101-123
    String fileUrl, // 파일
    Long chatroomId // 채팅방 아이디
) {
//    public static ApartmentInfoResponse from(Apartment apartment){
//        return new ApartmentInfoResponse(
//            apartment.getName(),
//            apartment.getRegion(),
//            apartment.getLocation(),
//            apartment.getTotalUnit(),
//            apartment.getParkingCapacity(),
//            apartment.getBuildingCount(),
//            apartment.getBuildingRange(),
//            apartment.getFileUrl(),
//            apartment.getChatroomId()
//        );
//    }

    public static ApartmentInfoResponse from(ApartmentBasicInfo<Body<BasicItem>> basic, ApartmentDetailInfo<Body<DetailItem>> detail, Apartment apartment) {
        BasicItem basicItem = basic.getResponse().getBody().getItem();
        DetailItem detailItem = detail.getResponse().getBody().getItem();

        return ApartmentInfoResponse.builder()
                .kaptCode(basicItem.getKaptCode())
                .kaptName(basicItem.getKaptName())
                .kaptUsedate(basicItem.getKaptUsedate())
                .kaptdaCnt(basicItem.getKaptdaCnt())
                .kaptDongCnt(basicItem.getKaptDongCnt())
                .codeAptNm(basicItem.getCodeAptNm())
                .kaptTopFloor(basicItem.getKaptTopFloor())
                .kaptMparea60(basicItem.getKaptMparea60())
                .kaptMparea85(basicItem.getKaptMparea85())
                .kaptMparea135(basicItem.getKaptMparea135())
                .kaptMparea136(basicItem.getKaptMparea136())
                .kaptTel(basicItem.getKaptTel())
                .kaptdPcnt(detailItem.getKaptdPcnt())
                .kaptdPcntu(detailItem.getKaptdPcntu())
                .codeHeatNm(basicItem.getCodeHeatNm())
                .kaptdWtimebus(detailItem.getKaptdWtimebus())
                .subwayLine(detailItem.getSubwayLine())
                .subwayStation(detailItem.getSubwayStation())
                .kaptdWtimesub(detailItem.getKaptdWtimesub())
                .convenientFacility(detailItem.getConvenientFacility())
                .educationFacility(detailItem.getEducationFacility())
                .groundElChargerCnt(detailItem.getGroundElChargerCnt())
                .undergroundElChargerCnt(detailItem.getUndergroundElChargerCnt())
                .welfareFacility(detailItem.getWelfareFacility())
                .kaptdCccnt(detailItem.getKaptdCccnt())

                .name(apartment.getName())
                .region(apartment.getRegion())
                .location(apartment.getLocation())
                .totalUnit(apartment.getTotalUnit())
                .parkingCapacity(apartment.getParkingCapacity())
                .buildingCount(apartment.getBuildingCount())
                .buildingRange(apartment.getBuildingRange())
                .fileUrl(apartment.getFileUrl())
                .chatroomId(apartment.getChatroomId())
                .build();
    }

}
