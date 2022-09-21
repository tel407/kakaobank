package com.code.kakaobank.search.payload;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class SearchBlogDto {

    /**
     * ==============================================================================================
     * 블로그 조회 Request Dto (application)
     * ----------------------------------------------------------------------------------------------
     */
    @Getter
    @Setter
    public static class SearchBlogRequestDto {
        private String word = "";   // 검색을 원하는 질의어
        private String sort = "accuracy";    //결과 문서 정렬 방식
        private Integer page = 1;   // 보여질 페이지 넘버
        private Integer cnt = 10;    // 보여질 컨텐츠 갯수
    }

    /**
     * ==============================================================================================
     * 블로그 조회 결과 Response Dto (application) 어떤 API 를 쓰던 Application 에 맞추는 DTO
     * ----------------------------------------------------------------------------------------------
     */
    @Getter
    @Builder
    public static class SearchBlogResult {
        private int totalCount;	        //	검색된 문서 수
        private int pageableCount;	    //	total_count 중 노출 가능 문서 수
        private boolean isEnd;	        //	마지막 페이지 여부
        private ArrayList<SearchBlogDto.SearchBlogItem> SearchBlogList; //	내용배열
    }

    /**
     * ==============================================================================================
     * 블로그 조회 결과 항목 Dto (application)
     * ----------------------------------------------------------------------------------------------
     */
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
