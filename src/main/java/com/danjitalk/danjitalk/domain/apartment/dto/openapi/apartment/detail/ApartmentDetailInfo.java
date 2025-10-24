package com.danjitalk.danjitalk.domain.apartment.dto.openapi.apartment.detail;

import com.danjitalk.danjitalk.domain.apartment.dto.openapi.common.Response;
import lombok.Getter;

@Getter
public class ApartmentDetailInfo<T> {

    private Response<T> response;
}
