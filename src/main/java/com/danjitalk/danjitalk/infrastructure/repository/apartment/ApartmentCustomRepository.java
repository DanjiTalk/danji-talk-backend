package com.danjitalk.danjitalk.infrastructure.repository.apartment;


import com.danjitalk.danjitalk.domain.search.dto.ApartmentSearchResponse;
import java.util.List;

public interface ApartmentCustomRepository {

    List<ApartmentSearchResponse> findByKeywordWithCursor(String keyword, Long cursor, long limit);
    long countByKeyword(String keyword);
}
