package com.danjitalk.danjitalk.application.search;

import com.danjitalk.danjitalk.domain.search.dto.ApartmentSearchResponse;
import com.danjitalk.danjitalk.domain.search.dto.ApartmentSearchResultResponse;
import com.danjitalk.danjitalk.infrastructure.repository.apartment.ApartmentRepository;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final ApartmentRepository apartmentRepository;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 아파트 단지 검색
     * @param keyword
     * @return
     */
    @Transactional(readOnly = true)
    public ApartmentSearchResultResponse searchApartments(String keyword, Long cursor, long limit) {
        // TODO: 북마크 여부 추가하기
        String countKey = "apartment:count:" + keyword;
        String resultCount = redisTemplate.opsForValue().get(countKey);

        // TODO: 불필요한 데이터를 조회해서 캐싱되지 않기위해 keyword 걸러야할듯
        if (resultCount == null) {
            resultCount = String.valueOf(apartmentRepository.countByKeyword(keyword));
            redisTemplate.opsForValue().set(countKey, resultCount, Duration.ofMinutes(10));
        }

        List<ApartmentSearchResponse> apartmentSearchResponses = apartmentRepository.findByKeywordWithCursor(keyword, cursor, limit + 1);
        boolean lastPage = apartmentSearchResponses.size() <= limit; // true => 이후 데이터 없음
        if (!lastPage) {
            apartmentSearchResponses.remove((int) limit); // 초과분 제거
        }
        return new ApartmentSearchResultResponse(apartmentSearchResponses, Long.valueOf(resultCount), lastPage);
    }
}
