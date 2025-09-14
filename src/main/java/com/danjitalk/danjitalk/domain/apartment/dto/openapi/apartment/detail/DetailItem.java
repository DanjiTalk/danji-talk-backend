package com.danjitalk.danjitalk.domain.apartment.dto.openapi.apartment.detail;

import lombok.Getter;

@Getter
public class DetailItem {

    private String kaptCode;         // 단지 코드
    private String kaptName;         // 단지명
    private String codeMgr;          // 관리방식
    private String kaptMgrCnt;       // 관리인원수
    private String kaptCcompany;     // 관리업체명
    private String codeSec;          // 경비방식
    private String kaptdScnt;        // 경비 인원
    private String kaptdSecCom;      // 경비 업체명
    private String codeClean;        // 청소 방식
    private String kaptdClcnt;       // 청소 인원
    private String codeGarbage;      // 쓰레기 처리 방식
    private String codeDisinf;       // 소독 방식
    private String kaptdDcnt;        // 소독 인원
    private String disposalType;     // 소독 방법
    private String codeStr;          // 건물 구조
    private String kaptdEcapa;       // 발전 용량
    private String codeEcon;         // 난방 방식
    private String codeEmgr;         // 전기 관리
    private String codeFalarm;       // 화재 경보기
    private String codeWsupply;      // 급수 방식
    private String codeElev;         // 승강기 관리
    private Integer kaptdEcnt;       // 승강기 수
    private String kaptdPcnt;        // 주차대수
    private String kaptdPcntu;       //  주차대수 지하
    private String codeNet;          // 주차관제·홈네트워크 여부
    private String kaptdCccnt;       // CCTV 대수
    private String welfareFacility;  // 복지시설
    private String kaptdWtimebus;    // 버스정류장 접근성
    private String subwayLine;       // 지하철 노선
    private String subwayStation;    // 지하철역
    private String kaptdWtimesub;    // 지하철 접근 시간
    private String convenientFacility; // 편의시설
    private String educationFacility;  // 교육시설
    private Integer groundElChargerCnt;     // 지상 전기차 충전기
    private Integer undergroundElChargerCnt;// 지하 전기차 충전기
    private String useYn;            // 사용 여부
}
