package com.danjitalk.danjitalk.application.search;

import com.danjitalk.danjitalk.domain.search.dto.ApartmentSearchResponse;
import com.danjitalk.danjitalk.domain.search.dto.ApartmentSearchResultResponse;
import com.danjitalk.danjitalk.infrastructure.repository.apartment.ApartmentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final ApartmentRepository apartmentRepository;

    /**
     * 아파트 단지 검색
     * @param keyword
     * @return
     */
    @Transactional(readOnly = true)
    public ApartmentSearchResultResponse searchApartments(String keyword, Long cursor, long limit) {
        // TODO: 북마크 여부 추가하기
        boolean isFirstPage = cursor == null;
        Long totalCount = isFirstPage ? apartmentRepository.countByKeyword(keyword) : null;

        List<ApartmentSearchResponse> apartmentSearchResponses = apartmentRepository.findByKeywordWithCursor(keyword, cursor, limit);
        boolean lastPage = false;

        if (apartmentSearchResponses.isEmpty()) {
            lastPage = true;
        }

        return new ApartmentSearchResultResponse(apartmentSearchResponses, totalCount, lastPage);
    }
}
