package com.danjitalk.danjitalk.infrastructure.repository.apartment;


import com.danjitalk.danjitalk.domain.search.dto.ApartmentSearchResponse;
import java.util.List;

public interface ApartmentCustomRepository {

    List<ApartmentSearchResponse> findByKeyword(String keyword);
}
