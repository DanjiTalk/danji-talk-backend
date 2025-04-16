package com.danjitalk.danjitalk.application.search;

import com.danjitalk.danjitalk.domain.search.dto.ApartmentSearchResponse;
import com.danjitalk.danjitalk.infrastructure.repository.apartment.ApartmentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final ApartmentRepository apartmentRepository;

    /**
     * 아파트 단지 검색
     * @param keyword
     * @return
     */
    public List<ApartmentSearchResponse> searchApartments(String keyword) {
        // TODO: 북마크 여부 추가하기
        return apartmentRepository.findByKeyword(keyword);
    }
}
