package com.danjitalk.danjitalk.application.search;

import com.danjitalk.danjitalk.common.util.SecurityContextHolderUtil;
import com.danjitalk.danjitalk.domain.search.dto.ApartmentSearchResponse;
import com.danjitalk.danjitalk.domain.search.dto.ApartmentSearchResultResponse;
import com.danjitalk.danjitalk.infrastructure.repository.apartment.ApartmentRepository;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SearchService {

    private static final int MAXIMUM_SAVED_VALUE = 5;
    private static final String SEARCH_MEMBER_KEY = "search:member";

    private final ApartmentRepository apartmentRepository;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 아파트 단지 검색
     * @param keyword
     * @return
     */
    @Transactional(readOnly = true)
    public ApartmentSearchResultResponse searchApartments(String keyword, Long cursor, long limit) {
        Long memberId = SecurityContextHolderUtil.getMemberIdOptional().orElse(0L);

        // TODO: 북마크 여부 추가하기
        // 아파트 검색 결과 수 저장 키
        String countKey = "apartment:result:count:" + keyword;
        String resultCount = redisTemplate.opsForValue().get(countKey);

        // TODO: 불필요한 데이터를 조회해서 캐싱되지 않기위해 keyword 걸러야할듯
        if (resultCount == null) {
            resultCount = String.valueOf(apartmentRepository.countByKeyword(keyword));
            redisTemplate.opsForValue().set(countKey, resultCount, Duration.ofMinutes(10));
        }

        if (memberId != 0L) { // 회원이면
            String key = SEARCH_MEMBER_KEY + memberId;

            if (!resultCount.equals("0")) {
                addKeywordInRedis(keyword, key);
            }
        }

        List<ApartmentSearchResponse> apartmentSearchResponses = apartmentRepository.findByKeywordWithCursor(keyword, cursor, limit + 1);
        boolean lastPage = apartmentSearchResponses.size() <= limit; // true => 이후 데이터 없음
        if (!lastPage) {
            apartmentSearchResponses.remove((int) limit); // 초과분 제거
        }
        return new ApartmentSearchResultResponse(apartmentSearchResponses, Long.valueOf(resultCount), lastPage);
    }

    /**
     * 개인 검색 기록
     * @param keyword
     * @param key 검색한 사람
     */
    private void addKeywordInRedis(String keyword, String key) {
        // Redis List에 최근 검색어 추가
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        listOperations.remove(key, 0, keyword);
        listOperations.leftPush(key, keyword);
        Long size = listOperations.size(key);

        if (size != null && size > MAXIMUM_SAVED_VALUE) {
            listOperations.trim(key, 0, MAXIMUM_SAVED_VALUE - 1);
        }
    }
}
