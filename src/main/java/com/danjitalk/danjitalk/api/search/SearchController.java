package com.danjitalk.danjitalk.api.search;

import com.danjitalk.danjitalk.application.search.SearchService;
import com.danjitalk.danjitalk.common.response.ApiResponse;
import com.danjitalk.danjitalk.domain.search.dto.ApartmentSearchResultResponse;
import com.danjitalk.danjitalk.domain.search.dto.PopularKeywordResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<ApiResponse<ApartmentSearchResultResponse>> searchApartments(
        @RequestParam String keyword,
        @RequestParam(required = false) Long lastIndex,
        @RequestParam(required = false, defaultValue = "20") long limit
    ) {
        ApartmentSearchResultResponse apartmentSearchResultResponse =  searchService.searchApartments(keyword, lastIndex, limit);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), null, apartmentSearchResultResponse));
    }

    @GetMapping("/popular-keywords")
    public ResponseEntity<ApiResponse<List<PopularKeywordResponse>>> searchPopularKeywords(
        @RequestParam(required = false, defaultValue = "5") Long limit
    ) {
        List<PopularKeywordResponse> popularKeywordResponses = searchService.getTopKeywords(limit);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), null, popularKeywordResponses));
    }

}
