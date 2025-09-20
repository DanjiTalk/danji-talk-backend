package com.danjitalk.danjitalk.openapi.location.service;

import com.danjitalk.danjitalk.common.exception.BaseException;
import com.danjitalk.danjitalk.openapi.location.dto.StanReginCdResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationService {

    @Value("${openapi.secretkey}")
    private String serviceKey;

    private final ObjectMapper objectMapper;
    private final RestClient restClient;

    // 행정안전부_행정표준코드_법정동코드
    // 법정동코드 정보의 지역코드, 시도코드, 읍면동코드, 리코드, 지역주소명 등을 조회한다.
    // location 검색을 통해 시군구 코드 리스트 반환
    public List<String> getLegalDongCode(String location, Integer pageNo, Integer numOfRows) {
        if (location == null || location.isEmpty()) {
            throw new IllegalArgumentException("location is null or empty");
        }
        pageNo = (pageNo == null || pageNo < 1) ? 1 : pageNo;
        numOfRows = (numOfRows == null || numOfRows < 1) ? 1000 : numOfRows;

        String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8);

        URI uri = UriComponentsBuilder.fromUriString("https://apis.data.go.kr/1741000/StanReginCd/getStanReginCdList")
                .queryParam("serviceKey", serviceKey)
                .queryParam("pageNo", String.valueOf(pageNo)) // 페이지번호
                .queryParam("numOfRows", String.valueOf(numOfRows)) // 한 페이지 결과 수
                .queryParam("type", "json")
                .queryParam("locatadd_nm", encodedLocation)
                .build(true) // 이미 인코딩된 키 인코딩 방지
                .toUri();

        log.info("최종 URI: {}" , uri);

        String responseString = restClient.get()
                .uri(uri)
                .accept(MediaType.TEXT_HTML) // 응답이 content-type text/html 임... MediaType.APPLICATION_JSON하면 HTTP ROUTING ERROR 에러남
                .retrieve()
                .toEntity(String.class).getBody();

        log.info("responseString: {}", responseString);

        StanReginCdResponse stanReginCdResponse = null;
        try {
            stanReginCdResponse = objectMapper.readValue(responseString, StanReginCdResponse.class);
        } catch (Exception e) {
            log.error("파싱 실패 에러 메시지 {}", e.getMessage());
            throw new BaseException(502, "LegalDongCode JSON 파싱 실패");
        }

        List<String> sigunguCodes = stanReginCdResponse.getStanReginCd().get(1).getRow().stream()
                .map(e -> e.getRegion_cd().substring(0, 5))// 법정동 앞 5자리 추출(시군구코드로)
                .distinct()
                .toList();

        return sigunguCodes;
    }

}
