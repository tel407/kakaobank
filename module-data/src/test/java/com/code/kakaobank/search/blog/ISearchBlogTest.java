package com.code.kakaobank.search.blog;

import com.code.kakaobank.search.payload.SearchBlogDto;

import java.util.Map;

public interface ISearchBlogTest<T> {
    Map<String, Object> validRequestParam(T searchBlogDto) throws Exception;
    Map<String, Object> apiConnect(SearchBlogDto.SearchBlogRequestDto searchParam, boolean forceError);
    SearchBlogDto.SearchBlogResult getSerchBlogResult();
}
