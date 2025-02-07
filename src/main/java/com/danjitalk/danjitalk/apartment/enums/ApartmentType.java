package com.danjitalk.danjitalk.apartment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApartmentType {

    APT("아파트"),
    VILLA("빌라"),
    OFFICE("오피스텔");

    private final String description;

}
