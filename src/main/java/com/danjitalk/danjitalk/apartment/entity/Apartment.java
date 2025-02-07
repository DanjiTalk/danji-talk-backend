package com.danjitalk.danjitalk.apartment.entity;

import com.danjitalk.danjitalk.apartment.enums.ApartmentType;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Apartment {

    @Id
    @Column(length = 32, nullable = false, unique = true)
    private String id;

    private String name;                    // 아파트 이름

    private String region;                  // 아파트 지역

    private String location;                // 아파트 위치

    private Integer totalUnit;              // 총 세대 수

    private Integer parkingCapacity;        // 주차 가능 대수

    @Enumerated(EnumType.STRING)
    private ApartmentType aptType;                // 아파트 타입

}
