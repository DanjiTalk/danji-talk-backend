package com.danjitalk.danjitalk.openapi.location.dto;

import lombok.Getter;

@Getter
public class Row {
    private String region_cd; //지역코드
    private String sido_cd; //시도코드
    private String sgg_cd; //시군구코드, 시도코드랑 합쳐야함..
    private String umd_cd; //읍면동코드
    private String ri_cd; //리코드
    private String locatjumin_cd; //지역코드_주민
    private String locatjijuk_cd; //지역코드_지적
    private String locatadd_nm; //지역주소명
    private int locat_order; //서열??
    private String locat_rm; //비고
    private String locathigh_cd; //상위지역코드
    private String locallow_nm; //최하위지역명
    private String adpt_de; // 생성일
}
