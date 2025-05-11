package com.danjitalk.danjitalk.domain.user.member.entity;

import com.danjitalk.danjitalk.domain.apartment.entity.Apartment;
import com.danjitalk.danjitalk.domain.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberApartment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Apartment apartment;

    private String building;
    private String unit;
    private LocalDate moveInDate;
    private Integer numberOfResidents;
    private String carNumbers;

    @Builder
    public MemberApartment(Member member, Apartment apartment, String building, String unit, LocalDate moveInDate,
        Integer numberOfResidents, String carNumbers) {
        this.member = member;
        this.apartment = apartment;
        this.building = building;
        this.unit = unit;
        this.moveInDate = moveInDate;
        this.numberOfResidents = numberOfResidents;
        this.carNumbers = carNumbers;
    }

    public void updateResidenceInfo(
        Apartment apartment,
        String building,
        String unit,
        LocalDate moveInDate,
        Integer numberOfResidents,
        String carNumbers
    ) {
        this.apartment = apartment;
        this.building = building;
        this.unit = unit;
        this.moveInDate = moveInDate;
        this.numberOfResidents = numberOfResidents;
        this.carNumbers = carNumbers;
    }
}
