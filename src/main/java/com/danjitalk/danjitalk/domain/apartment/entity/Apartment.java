package com.danjitalk.danjitalk.domain.apartment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

}
