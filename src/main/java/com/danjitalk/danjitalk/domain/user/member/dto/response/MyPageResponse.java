package com.danjitalk.danjitalk.domain.user.member.dto.response;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public class MyPageResponse{
    private String fileId;
    private String name;
    private String nickname;
    private String email;
    private String phoneNumber; // 프로필 수정 부분을 위한 데이터

    private Long memberApartmentId;
    private Long apartmentId;
    private String apartmentName;
    private String region;
    private String location;
    private String building;
    private String unit;

    private LocalDate moveInDate;
    private Integer numberOfResidents;
    private List<String> carNumbers;

    public MyPageResponse(String fileId, String name, String nickname, String email, String phoneNumber,
        Long memberApartmentId, Long apartmentId, String apartmentName, String region, String location, String building,
        String unit, LocalDate moveInDate, Integer numberOfResidents) {
        this.fileId = fileId;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.memberApartmentId = memberApartmentId;
        this.apartmentId = apartmentId;
        this.apartmentName = apartmentName;
        this.region = region;
        this.location = location;
        this.building = building;
        this.unit = unit;
        this.moveInDate = moveInDate;
        this.numberOfResidents = numberOfResidents;
    }

    public void setCarNumbers(String carNumbers) {
        this.carNumbers = Arrays.asList(carNumbers.split(","));
    }
}
