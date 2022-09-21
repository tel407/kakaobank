package com.code.kakaobank.api.search.controller;

import com.code.kakaobank.search.service.SearchBlogService;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping(value="/rank", produces = "application/json")
public class SearchRankController {

	@Autowired private SearchBlogService searchBlogService;

	/**
	 * ==============================================================================================
	 * 블로그 검색 API Contoller
	 * ----------------------------------------------------------------------------------------------
	 */
	@RequestMapping(value = "/keyword", method = RequestMethod.GET)
	public String getSearchRankByKeword(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> rstMap = searchBlogService.getSearchRankByKeyword();
		String formattedData = new GsonBuilder().setPrettyPrinting().create().toJson(rstMap); //JSON 정렬
		return formattedData;
	}
}
