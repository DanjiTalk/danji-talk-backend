package com.danjitalk.danjitalk.domain.apartment.dto.openapi.apartment.basic;

import lombok.Getter;
import software.amazon.awssdk.core.Response;

@Getter
public class ApartmentBasicInfo<T> {

    private Response<T> response;
}
