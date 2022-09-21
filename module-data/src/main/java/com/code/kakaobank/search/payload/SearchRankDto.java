package com.code.kakaobank.search.payload;


import lombok.Builder;
import lombok.Getter;

/**
 * ==============================================================================================
 * 인기 검색어 목록 조회 결과 항목 Dto (application)
 * ----------------------------------------------------------------------------------------------
 */
@Builder
@Getter
public class SearchRankDto {
    private String keyword;   // 검색을 원하는 질의어
    private int score;    //결과 문서 정렬 방식
    private int rank;   // 검색을 원하는 질의어
}
