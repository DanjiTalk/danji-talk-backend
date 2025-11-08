package com.danjitalk.danjitalk.domain.apartment.entity;

import com.danjitalk.danjitalk.domain.community.feed.entity.Feed;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Apartment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;                    // 아파트 이름

    private String region;                  // 아파트 지역

    private String location;                // 아파트 위치

    private Integer totalUnit;              // 총 세대 수

    private Integer parkingCapacity;        // 주차 가능 대수

    private Integer buildingCount;          // 동 수

    private String buildingRange;           // 동 범위

    private String fileUrl;

    private String thumbnailFileUrl;

    private Long chatroomId;

    private String kaptCode;  // 단지코드

    private String kaptName;       // 단지명

    @Embedded
    private BasicInfo basicInfo;

    @Embedded
    private DetailInfo detailInfo;

    @OneToMany(mappedBy = "apartment")
    private List<Feed> feedList = new ArrayList<>();

    @Builder
    public Apartment(String name, String region, String location, Integer totalUnit, Integer parkingCapacity,
        Integer buildingCount, String buildingRange, String fileUrl, String thumbnailFileUrl, String kaptCode,
        String kaptName, BasicInfo basicInfo, DetailInfo detailInfo) {
        this.name = name;
        this.region = region;
        this.location = location;
        this.totalUnit = totalUnit;
        this.parkingCapacity = parkingCapacity;
        this.buildingCount = buildingCount;
        this.buildingRange = buildingRange;
        this.fileUrl = fileUrl;
        this.thumbnailFileUrl = thumbnailFileUrl;
        this.kaptCode = kaptCode;
        this.kaptName = kaptName;
        this.basicInfo = basicInfo;
        this.detailInfo = detailInfo;
    }

    public void addChatroom(Long chatroomId) {
        this.chatroomId = chatroomId;
    }
}
