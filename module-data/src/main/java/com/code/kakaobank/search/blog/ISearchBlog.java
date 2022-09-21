package com.code.kakaobank.search.blog;

import com.code.kakaobank.search.payload.SearchBlogDto;

import java.util.Map;

public interface ISearchBlog<T> {
    Map<String, Object> validRequestParam(T searchBlogDto) throws Exception;
    Map<String, Object> apiConnect(T searchBlogDto);
    SearchBlogDto.SearchBlogResult getSerchBlogResult();
}
