package com.code.kakaobank.search.service;

import com.code.kakaobank.search.payload.SearchBlogDto;

import java.util.Map;

public interface SearchBlogServiceTest {
    Map<String,Object> getSearchBlogByKeyword(SearchBlogDto.SearchBlogRequestDto searchParam);
    Map<String,Object> getSearchRankByKeyword();
    SearchBlogDto.SearchBlogRequestDto validPageParam(SearchBlogDto.SearchBlogRequestDto searchParam);
    Map<String,Object> callSearchBlogApiModule(SearchBlogDto.SearchBlogRequestDto search);
    void updateKewordForRDBMS(String searchWord);

}
