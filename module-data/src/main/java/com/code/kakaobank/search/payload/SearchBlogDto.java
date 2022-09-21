package com.code.kakaobank.search.payload;


import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class SearchBlogDto {
    private String searchword;   // 검색을 원하는 질의어
    private String searchsort = "accuracy";    //결과 문서 정렬 방식
    private Integer pageNumber;   // 검색을 원하는 질의어
    private Integer pageSize;    //결과 문서 정렬 방식

    @Getter
    @Builder
    public static class SearchBlogResult {
        private int totalCount;	        //	검색된 문서 수
        private int pageableCount;	    //	total_count 중 노출 가능 문서 수
        private boolean isEnd;	        //	마지막 페이지 여부
        private ArrayList<SearchBlogDto.SearchBlogItem> SearchBlogList; //	내용배열
    }

    @Builder
    @Getter
    public static class SearchBlogItem {
        private String title;	    //	블로그 글 제목
        private String contents;	//	블로그 글 요약
        private String url;	        //	블로그 글 URL
        private String blogname;	//	블로그의 이름
        private String thumbnail;	//	검색 시스템에서 추출한 대표 미리보기 이미지 URL, 미리보기 크기 및 화질은 변경될 수 있음
        private String datetime;	//	블로그 글 작성시간, ISO 8601
    }
}
