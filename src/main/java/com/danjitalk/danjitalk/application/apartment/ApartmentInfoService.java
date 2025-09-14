package com.danjitalk.danjitalk.application.apartment;

import com.danjitalk.danjitalk.domain.apartment.dto.openapi.apartment.Body;
import com.danjitalk.danjitalk.domain.apartment.dto.openapi.apartment.basic.ApartmentBasicInfo;
import com.danjitalk.danjitalk.domain.apartment.dto.openapi.apartment.basic.BasicItem;
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
public class ApartmentInfoService {

    @Value("${openapi.secretkey}")
    private String serviceKey;

    private final RestClient restClient;

    public ApartmentBasicInfo<Body<BasicItem>> getAptBasicInfo(String kaptCode) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://apis.data.go.kr/1613000/AptBasisInfoServiceV4/getAphusBassInfoV4") // 기본정보 주소
                .queryParam("serviceKey", serviceKey)
                .queryParam("kaptCode", kaptCode) // 단지코드
                .build(true) // 이미 인코딩된 키 인코딩 방지
                .toUri();

        log.info("최종 URI: {}", uri);

        ApartmentBasicInfo<Body<BasicItem>> basicInfo = restClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<ApartmentBasicInfo<Body<BasicItem>>>() {});

        return basicInfo;
    }
}
