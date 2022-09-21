package com.code.kakaobank.api.search.controller;
import com.code.kakaobank.search.payload.SearchBlogDto;
import com.code.kakaobank.search.service.SearchBlogService;
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

	@RequestMapping(value = "/blog", method = RequestMethod.GET)
	public Map<String,Object> getSearchBlog(SearchBlogDto search, HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> rstMap = searchBlogService.getSearchBlogByKeyword(search);
		return rstMap;
	}
}
