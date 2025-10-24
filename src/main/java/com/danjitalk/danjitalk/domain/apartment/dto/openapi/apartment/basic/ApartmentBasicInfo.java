package com.danjitalk.danjitalk.domain.apartment.dto.openapi.apartment.basic;

import com.danjitalk.danjitalk.domain.apartment.dto.openapi.common.Response;
import lombok.Getter;

@Getter
public class ApartmentBasicInfo<T> {

    private Response<T> response;
}
