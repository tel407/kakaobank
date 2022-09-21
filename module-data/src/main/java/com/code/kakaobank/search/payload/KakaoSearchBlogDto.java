package com.code.kakaobank.search.payload;


import lombok.*;


/**
 * ==============================================================================================
 * 카카오 블로그 조회 API Request Dto
 * ----------------------------------------------------------------------------------------------
 */
@Getter
@Builder
public class KakaoSearchBlogDto extends SearchBlogDto {
	private String query; // 검색을 원하는 질의어
	private Integer page; // 결과 페이지 번호, 1~50 사이의 값, 기본 값
	private Integer size; // 한 페이지에 보여질 문서 수, 1~50 사이의 값, 기본 값 10
	private String sort; //결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy
}
