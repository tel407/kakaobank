package com.code.kakaobank.search.service;

import com.code.kakaobank.search.entity.SearchKeywordScore;
import com.code.kakaobank.search.payload.SearchBlogDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface SearchBlogService {
    Map<String,Object> getSearchBlogByKeyword(SearchBlogDto.SearchBlogRequestDto searchParam);
    Map<String,Object> getSearchRankByKeyword();
    SearchBlogDto.SearchBlogRequestDto validPageParam(SearchBlogDto.SearchBlogRequestDto searchParam);

}
