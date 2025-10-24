package com.danjitalk.danjitalk.openapi.danjilist.dto;

import lombok.Getter;

@Getter
public class Item {

    private String kaptCode;   // 단지코드
    private String kaptName;   // 단지명
    private String bjdCode;    // 법정동 코드
    private String as1;        // 시도
    private String as2;        // 시군구
    private String as3;        // 읍면동
    private String as4;        // 리 (nullable)
}
