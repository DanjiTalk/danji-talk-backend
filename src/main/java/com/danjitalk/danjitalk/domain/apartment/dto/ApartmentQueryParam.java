package com.danjitalk.danjitalk.domain.apartment.dto;

import com.danjitalk.danjitalk.common.exception.BadRequestException;

public record ApartmentQueryParam(
    Long id,
    String kaptCode
) {
    public void validate() {
        if (id == null && kaptCode == null) {
            throw new BadRequestException("id 또는 kaptCode 중 하나는 필수입니다.");
        }
        if (id != null && kaptCode != null) {
            throw new BadRequestException("id와 kaptCode를 동시에 사용할 수 없습니다.");
        }
    }
}