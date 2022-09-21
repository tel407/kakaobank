package com.code.kakaobank.search.service.impl;

import com.code.kakaobank.search.blog.*;
import com.code.kakaobank.search.entity.SearchKeywordScore;
import com.code.kakaobank.search.payload.SearchBlogDto;
import com.code.kakaobank.search.payload.SearchRankDto;
import com.code.kakaobank.search.repository.SearchKeywordScoreiRepository;
import com.code.kakaobank.search.service.SearchBlogServiceTest;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchBlogServiceImplTest implements SearchBlogServiceTest {

    @Autowired private KakaoSearchBlogTest kakaoSearchBlog;
    @Autowired private NaverSearchBlogTest naverSearchBlog;
    @Autowired private SearchKeywordScoreiRepository searchKeywordScoreiRepository;

    /**
     * ==============================================================================================
     * 블로그 검색 결과 조회
     * ----------------------------------------------------------------------------------------------
     */
    @Override
    public Map<String,Object> getSearchBlogByKeyword(SearchBlogDto.SearchBlogRequestDto searchParam) {
        Map<String,Object> rsltMap = new HashMap<>();
        rsltMap.put("status","ERROR");

        SearchBlogDto.SearchBlogRequestDto search = this.validPageParam(searchParam);
        Map<String,Object> moduleCallResult = this.callSearchBlogApiModule(search);

        Map<String,Object> connectResult = (Map<String,Object>) moduleCallResult.get("connectResult"); // 모듈결과
        ISearchBlog searchBlog = (ISearchBlog) moduleCallResult.get("searchBlog"); // 요청한 객체
        String isSuccess = (String) moduleCallResult.get("isSuccess"); //모듈 연계 처리결과
        String moduleName = (String) moduleCallResult.get("moduleName"); //처리한 모듈명

        //GET 블로그 조회 결과
        if("S".equals(isSuccess)){
            SearchBlogDto.SearchBlogResult searchBlogResult = searchBlog.getSerchBlogResult();
            this.updateKewordForRDBMS(search.getWord());
            //페이징 객체
            Page<SearchBlogDto.SearchBlogItem> pageing = new PageImpl<>(
                    searchBlogResult.getSearchBlogList(),
                    PageRequest.of(search.getPage() - 1, search.getCnt()),
                    searchBlogResult.getPageableCount()
            );
            rsltMap.put("status","SUCCESS");
            rsltMap.put("data",pageing);
        }

        rsltMap.put("moduleName", moduleName);
        rsltMap.put("messgae",connectResult.get("message"));

        return rsltMap;
    }


    /**
     * ==============================================================================================
     * 블로그 조회 API 모듈 (KAKAO, NAVER) 
     *  - KAKAO 모듈 실패시 NAVER 모듈로 전환
     * ----------------------------------------------------------------------------------------------
     */
    @Override
    public Map<String,Object> callSearchBlogApiModule(SearchBlogDto.SearchBlogRequestDto search){
        Map<String,Object> rsltMap = new HashMap<>();
        Map<String,Object> connectResult = new HashMap<>();
        ISearchBlogTest searchBlog = null; //결과값 도출하기위한 다형성
        String isSuccess = "N";
        String moduleName = "";

        // 요청 불량시 순차적 검색 API 사용 (KAKAO)
        if(!"S".equals(isSuccess)){
            searchBlog = kakaoSearchBlog;
            connectResult = kakaoSearchBlog.apiConnect(search,true);
            isSuccess = connectResult.get("status").toString();
            moduleName = kakaoSearchBlog.moduleName;
        }

        // 요청 불량시 순차적 검색 API 사용 (KAKAO)
        if(!"S".equals(isSuccess)){
            searchBlog = naverSearchBlog;
            connectResult = naverSearchBlog.apiConnect(search,false);
            isSuccess = connectResult.get("status").toString();
            moduleName = naverSearchBlog.moduleName;
        }

        rsltMap.put("searchBlog",searchBlog);
        rsltMap.put("connectResult",connectResult);
        rsltMap.put("isSuccess",isSuccess);
        rsltMap.put("moduleName",moduleName);
        return rsltMap;
    }
    /**
     * ==============================================================================================
     * 인기 검색어 목록 조회
     * ----------------------------------------------------------------------------------------------
     */
    @Override
    public Map<String,Object> getSearchRankByKeyword() {
        Map<String,Object> rsltMap = new HashMap<>();
        rsltMap.put("status","SUCCESS");
        List<SearchKeywordScore> topList =  searchKeywordScoreiRepository.findTop10ByOrderByScoreDesc();
        List<SearchRankDto> rankList = new ArrayList<>();
        int rankNum=0;
        for(SearchKeywordScore topITem: topList){
            rankNum++;
            rankList.add(SearchRankDto.builder()
                    .rank(rankNum)
                    .keyword(topITem.getKeyword())
                    .score(topITem.getScore())
                    .build()
            );
        }
        rsltMap.put("data",rankList);
        return rsltMap;
    }

    /**
     * ==============================================================================================
     * 페이징 정보 유효성 체크 및 기본값 으로 변환
     * ----------------------------------------------------------------------------------------------
     */
    @Override
    public SearchBlogDto.SearchBlogRequestDto validPageParam(SearchBlogDto.SearchBlogRequestDto searchParam) {
        int pageNumber = 1;
        int pageSize = 10;
        if(searchParam.getPage() != null && 0 < searchParam.getPage()){
            pageNumber = searchParam.getPage();
        }
        if(searchParam.getCnt() != null && 0 < searchParam.getCnt()){
            pageSize = searchParam.getCnt();
        }
        searchParam.setPage(pageNumber);
        searchParam.setCnt(pageSize);

        return searchParam;
    }

    /**
     * ==============================================================================================
     * searchWrod 검색어 형태소 분석
     * ----------------------------------------------------------------------------------------------
     */
    public List<String> getKeyWordList(String searchWord) {
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
        String document = searchWord;

        KomoranResult analyzeResultList = komoran.analyze(document);
        List<String> nounList = analyzeResultList.getNouns();
        return nounList;
    }

    private void updateKewordForREDIS(String searchWord){
        List<String> keyWordList = this.getKeyWordList(searchWord);
    }

    /**
     * ==============================================================================================
     * 검색시 키워드 조회 후 카운트
     * ----------------------------------------------------------------------------------------------
     */
    @Override
    public void updateKewordForRDBMS(String searchWord){
        List<String> keyWordList = this.getKeyWordList(searchWord);
        List<SearchKeywordScore> keywordList = searchKeywordScoreiRepository.findByKeywordInOrderByScoreDesc(keyWordList);
        List<SearchKeywordScore> saveList = new ArrayList<>();
        for(String searchKeyWord : keyWordList){
            boolean inBool = false;
            for( SearchKeywordScore keywordScore: keywordList){
                if(searchKeyWord.equals(keywordScore.getKeyword())){
                    inBool = true;
                    keywordScore.setScore(keywordScore.getScore() + 1);
                    saveList.add(keywordScore);
                    break;
                }
            }
            if(!inBool) saveList.add(SearchKeywordScore.builder().keyword(searchKeyWord).score(1).build());
        }
        searchKeywordScoreiRepository.saveAll(saveList);
    }
}
