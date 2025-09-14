package com.danjitalk.danjitalk.domain.apartment.dto.openapi.apartment.basic;

import lombok.Getter;

@Getter
public class BasicItem {

    private String kaptCode;       // 단지 코드
    private String kaptName;       // 단지명
    private String kaptAddr;       // 주소
    private String codeSaleNm;     // 분양/임대
    private String codeHeatNm;     // 난방 방식
    private Double kaptTarea;       // 대지면적
    private String kaptDongCnt;     // 동 수
    private Integer kaptdaCnt;      // 세대 수
    private String kaptBcompany;    // 관리업체
    private String kaptAcompany;    // 시공사
    private String kaptTel;         // 전화
    private String kaptUrl;         // 홈페이지
    private String codeAptNm;       // 건물 유형 아파트/주상복합 등
    private String doroJuso;        // 도로명 주소
    private String codeMgrNm;       // 관리 방식
    private String codeHallNm;      // 복도유형 계단식/복도식
    private String kaptUsedate;     // 사용 승인일
    private String kaptFax;         // 팩스
    private Integer hoCnt;          // 호 수
    private Double kaptMarea;       // 전용면적 총합
    private Integer kaptMparea60;   // 60㎡ 이하
    private Integer kaptMparea85;   // 85㎡ 이하
    private Integer kaptMparea135;  // 135㎡ 이하
    private Integer kaptMparea136;  // 136㎡ 이상
    private String privArea;        // 기타면적
    private String bjdCode;         // 법정동 코드
    private Integer kaptTopFloor;   // 최고 층
    private Integer ktownFlrNo;     // 총 층
    private Integer kaptBaseFloor;  // 기준 층
    private Integer kaptdEcntp;     // 뭔지 모르겠다
    private String zipcode;         // 우편번호

}