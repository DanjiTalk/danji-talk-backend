package com.danjitalk.danjitalk.openapi.common;

import lombok.Getter;

@Getter
public class Response<T>  {

    private T body;
    private Header header;
}
