package com.danjitalk.danjitalk.application.search;

import com.danjitalk.danjitalk.common.exception.BadRequestException;
import com.danjitalk.danjitalk.common.util.SecurityContextHolderUtil;
import com.danjitalk.danjitalk.domain.apartment.dto.RecentViewedApartment;
import com.danjitalk.danjitalk.domain.apartment.entity.Apartment;
import com.danjitalk.danjitalk.domain.bookmark.entity.Bookmark;
import com.danjitalk.danjitalk.domain.search.dto.ApartmentSearchResponse;
import com.danjitalk.danjitalk.domain.search.dto.ApartmentSearchResultResponse;
import com.danjitalk.danjitalk.domain.search.dto.PopularKeywordResponse;
import com.danjitalk.danjitalk.domain.search.dto.SearchKeywordResponse;
import com.danjitalk.danjitalk.infrastructure.repository.apartment.ApartmentRepository;
import com.danjitalk.danjitalk.infrastructure.repository.bookmark.BookmarkRepository;
import com.danjitalk.danjitalk.openapi.danjilist.dto.Body;
import com.danjitalk.danjitalk.openapi.danjilist.dto.Item;
import com.danjitalk.danjitalk.openapi.danjilist.dto.SigunguAptList3;
import com.danjitalk.danjitalk.openapi.danjilist.service.DanjiListService;
import com.danjitalk.danjitalk.openapi.location.service.LocationService;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class SearchService {

    private static final int MAXIMUM_SAVED_VALUE = 5;
    private static final String SEARCH_MEMBER_KEY = "search:member:";
    private static final String SEARCH_COUNT_KEY = "search:count:";

    private final ApartmentRepository apartmentRepository;
    private final BookmarkRepository bookmarkRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, Object> objectRedisTemplate;

    private final LocationService locationService;
    private final DanjiListService danjiListService;

    // 지역으로 검색하는건 그렇다 치는데 아파트 이름으로 검색하는건 검색이 안되는데..?
    /**
     * 아파트 단지 검색
     * @param keyword
     * @return
     */
    @Transactional(readOnly = true)
    public ApartmentSearchResultResponse searchApartments(String keyword, Long cursor, long limit) {
        if (isInvalidKeyword(keyword)){
            throw new BadRequestException("Invalid keyword");
        }

        Long memberId = SecurityContextHolderUtil.getMemberIdOptional().orElse(0L);

        // 검색을 지역으로 하면..
        List<String> locationCodes = locationService.getLegalDongCodeByLocation(keyword, 1, 1000); // 지역명인지 확인, 지역명 아니면 빈배열 받음
        log.info("locationCodeSize: {}", locationCodes.size());

        if (!locationCodes.isEmpty()) { // 지역명이라 결과있으면
            List<SigunguAptList3<Body>> SigunguAptList3s = new ArrayList<>();

            locationCodes.forEach(locationCode -> {
                SigunguAptList3<Body> list = danjiListService.getAptsBySigunguCode(Integer.parseInt(locationCode), 1, 1000);
                SigunguAptList3s.add(list);
                int RemainingPageCount = danjiListService.getRemainingPageCount(list); // 추가로 호출해야 하는 페이지 수 e.g. 1나오면 한페이지 더(2페이지 까지) 필요한 것

                for (int i = 2; i <= 1 + RemainingPageCount; i++) { // 추가로 호출해야 하는 페이지 호출 후 리스트에 추가
                    SigunguAptList3s.add(danjiListService.getAptsBySigunguCode(Integer.parseInt(locationCode), i, 1000));
                }
            });
            List<ApartmentSearchResponse> apartmentSearchResponses = new ArrayList<>();

            List<Item> allItems = SigunguAptList3s.stream()
                    .flatMap(e -> e.getResponse().getBody().getItems().stream())
                    .toList();

            List<String> kaptCodes = allItems.stream().map(Item::getKaptCode).toList();

            Map<String, Apartment> map = apartmentRepository.findByKaptCodeIn(kaptCodes).stream()
                    .collect(Collectors.toMap(Apartment::getKaptCode, Function.identity()));

            // 유저 북마크 조회
            Set<String> bookmarkedCodes = bookmarkRepository.findByMemberId(memberId)
                    .stream()
                    .map(Bookmark::getKaptCode)
                    .collect(Collectors.toSet());

            allItems.forEach(e -> {
                Apartment apartment = map.get(e.getKaptCode());
                if (apartment == null) {
                    apartmentSearchResponses.add(
                        new ApartmentSearchResponse(
                            null, e.getKaptName(), safeConcat(e.getAs1(), e.getAs2()),
                            safeConcat(e.getAs3(), e.getAs4()),
                            null, null, null, bookmarkedCodes.contains(e.getKaptCode()), e.getKaptCode()));
                } else {
                    apartmentSearchResponses.add(
                        new ApartmentSearchResponse(
                            apartment.getId(), e.getKaptName(), safeConcat(e.getAs1(), e.getAs2()), safeConcat(e.getAs3(), e.getAs4()),
                            apartment.getTotalUnit(), apartment.getBuildingCount(), apartment.getThumbnailFileUrl(), bookmarkedCodes.contains(e.getKaptCode()), e.getKaptCode()));
                }
            });
            return new ApartmentSearchResultResponse(apartmentSearchResponses, (long)allItems.size(), true);
        }

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
                addKeywordInRedis(keyword, key); // 사용자 검색어 저장
                incrementSearchCount(keyword); // 검색 키워드 카운트 증가
            }
        }

        List<ApartmentSearchResponse> apartmentSearchResponses = apartmentRepository.findByKeywordWithCursor(keyword, cursor, limit + 1, memberId);
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

    // 검색 키워드 조회 횟수 저장
    private void incrementSearchCount(String keyword) {
        // Redis에서 해당 검색어의 검색 횟수 증가
        redisTemplate.opsForZSet().incrementScore(SEARCH_COUNT_KEY, keyword, 1);
    }

    private boolean isInvalidKeyword(String keyword) {
        return keyword == null || keyword.isBlank() || keyword.isEmpty();
    }

    // 인기 Top 5 키워드 조회 (일 단위? 누적?)
    public List<PopularKeywordResponse> getTopKeywords(Long limit) {
        // Sorted Set에서 조회 횟수 기준으로 상위 5개 키워드 조회 (내림차순)
        Set<String> topKeywords = redisTemplate.opsForZSet().reverseRange(SEARCH_COUNT_KEY, 0, limit - 1);

        if (topKeywords == null) {
            return Collections.emptyList(); // 빈 리스트 반환
        }

        return topKeywords.stream()
                .map(PopularKeywordResponse::new)
                .toList();
    }

    public List<SearchKeywordResponse> getUserRecentSearchHistory(Long limit) {
        Long currentMemberId = SecurityContextHolderUtil.getMemberIdOptional().orElse(0L);

        if (currentMemberId == 0) {
            return Collections.emptyList();
        }

        String key = SEARCH_MEMBER_KEY + currentMemberId;

        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        List<String> recentComplexes = listOperations.range(key, 0, limit - 1);

        if(recentComplexes == null) {
            return Collections.emptyList();
        }

        return recentComplexes.stream()
                .map(SearchKeywordResponse::new)
                .toList();
    }

    public List<RecentViewedApartment> getRecentApartments() {
        Long currentMemberId = SecurityContextHolderUtil.getMemberIdOptional().orElse(0L);

        if (currentMemberId == 0) {
            return Collections.emptyList();
        }

        ListOperations<String, Object> listOperations = objectRedisTemplate.opsForList();

        List<Object> rawList = listOperations.range("recent:apartment:member:" + currentMemberId, 0, MAXIMUM_SAVED_VALUE - 1);

        if (rawList == null) {
            return Collections.emptyList();
        }

        return rawList.stream()
                .filter(RecentViewedApartment.class::isInstance)
                .map(RecentViewedApartment.class::cast)
                .toList();
        // TODO: 북마크 추가
    }

    private String safeConcat(String s1, String s2) {
        return (s1 == null ? "" : s1)  + " " + (s2 == null ? "" : s2);
    }
}
