package com.code.kakaobank.api.search.controller;
import com.code.kakaobank.search.payload.SearchBlogDto;
import com.code.kakaobank.search.service.SearchBlogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value="/search", produces = "application/json")
public class SearchController {

	@Autowired private SearchBlogService searchBlogService;

	/**
	 * ==============================================================================================
	 * 인기 검색어 목록 (10위 까지) API Contoller
	 * ----------------------------------------------------------------------------------------------
	 */
	@RequestMapping(value = "/blog", method = RequestMethod.GET)
	public String getSearchBlog(SearchBlogDto.SearchBlogRequestDto searchParam, HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> rstMap = searchBlogService.getSearchBlogByKeyword(searchParam);
		
		String formattedData=new GsonBuilder().setPrettyPrinting().create().toJson(rstMap); //JSON 정렬

		return formattedData;
	}
}
