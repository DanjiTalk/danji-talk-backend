package com.danjitalk.danjitalk.domain.apartment.dto;

import jakarta.validation.constraints.Pattern;

public record ApartmentRegisterRequest(
    String name,
    String region,
    String location,
    Integer totalUnit,
    Integer parkingCapacity,

    @Pattern(regexp = "^\\d+동 ~ \\d+동 \\(\\d+개동\\)$", message = "올바른 형식이 아닙니다. \"101동 ~ 123동 (23개동)\" 형식으로 입력해주세요.\n")
    String buildingRange
) {

}
