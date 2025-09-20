package com.danjitalk.danjitalk.openapi.danjilist.service;

import com.danjitalk.danjitalk.openapi.danjilist.dto.Body;
import com.danjitalk.danjitalk.openapi.danjilist.dto.SigunguAptList3;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class DanjiListService {

    @Value("${openapi.secretkey}")
    private String serviceKey;

    private final RestClient restClient;

    // 국토교통부_공동주택 단지 목록제공 서비스
    // 시도(2자리), 시군구(3자리), 읍면동(3자리), 리(2자리) 법정동코드 늘어나면 앞에 단위 코드 붙여야함
    // 검색가능 형태: 시도, 시군구, 법정동..
    // 법정동코드 == 지역코드 10자리.. 읍면동까지가 좋은데 없는듯..
    // 시군구 사용하려면 5자리
    // 국토교통부_시군구 아파트 목록, 국토교통부_법정동 아파트 목록 둘 중 하나 쓸 듯
    // 우선 시군구로 진행
    public SigunguAptList3<Body> getApts(Integer sigunguCode, Integer pageNo, Integer numOfRows) {
        if (sigunguCode == null) {
            throw new IllegalArgumentException("sigunguCode must not be null");
        }
        pageNo = pageNo == null ? 1 : pageNo;
        numOfRows = numOfRows == null ? 1000 : numOfRows;

        URI uri = UriComponentsBuilder
                .fromUriString("https://apis.data.go.kr/1613000/AptListService3/getSigunguAptList3")
                .queryParam("serviceKey", serviceKey)
                .queryParam("pageNo", String.valueOf(pageNo)) // 페이지번호
                .queryParam("numOfRows", String.valueOf(numOfRows)) // 한 페이지 결과 수
                .queryParam("sigunguCode", String.valueOf(sigunguCode)) // 시군구 코드 5자리
                .build(true) // 이미 인코딩된 키 인코딩 방지
                .toUri();

        log.info("최종 URI: {}" , uri);

        SigunguAptList3<Body> sigunguAptList3 = restClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<SigunguAptList3<Body>>() {});

        return sigunguAptList3;
    }

    public int getRemainingPageCount(SigunguAptList3<Body> data) {
        int pageSize = 1000;
        int totalCount = data.getResponse().getBody().getTotalCount();

        int remaining = totalCount - pageSize; // 남은 데이터 수

        if (remaining <= 0) {
            return 0;
        }
        return (remaining + pageSize - 1) / pageSize;
    }
}
