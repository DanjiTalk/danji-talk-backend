package com.danjitalk.danjitalk.domain.search.dto;

import java.util.List;

public record ApartmentSearchResultResponse(
    List<ApartmentSearchResponse> apartments,
    Long totalResultCount,
    Boolean lastPage
) {

}
