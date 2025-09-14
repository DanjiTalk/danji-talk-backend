package com.danjitalk.danjitalk.domain.apartment.dto.openapi.common;

import lombok.Getter;

@Getter
public class Response<T>  {

    private T body;
    private Header header;
}
