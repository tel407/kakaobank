package com.code.kakaobank.search.payload;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NaverSearchBlogDto extends SearchBlogDto {
	private String query; // 검색어. UTF-8로 인코딩되어야 합니다.
	private Integer display; // 한 번에 표시할 검색 결과 개수(기본값: 10, 최댓값: 100)
	private Integer start; // 검색 시작 위치(기본값: 1, 최댓값: 1000)
	private String sort; //검색 결과 정렬 방법- sim: 정확도순으로 내림차순 정렬(기본값)- date: 날짜순으로 내림차순 정렬
}
