package com.code.kakaobank.search.service.impl;

import com.code.kakaobank.search.blog.ISearchBlog;
import com.code.kakaobank.search.blog.KakaoSearchBlog;
import com.code.kakaobank.search.blog.NaverSearchBlog;
import com.code.kakaobank.search.entity.SearchKeywordScore;
import com.code.kakaobank.search.payload.KakaoSearchBlogDto;
import com.code.kakaobank.search.payload.NaverSearchBlogDto;
import com.code.kakaobank.search.payload.SearchBlogDto;
import com.code.kakaobank.search.payload.SearchRankDto;
import com.code.kakaobank.search.repository.SearchKeywordScoreiRepository;
import com.code.kakaobank.search.service.SearchBlogService;
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
public class SearchBlogServiceImpl  implements SearchBlogService {

    @Autowired private KakaoSearchBlog kakaoSearchBlog;
    @Autowired private NaverSearchBlog naverSearchBlog;
    @Autowired private SearchKeywordScoreiRepository searchKeywordScoreiRepository;


    /**
     * ==============================================================================================
     * 블로그 검색 결과 조회
     * ----------------------------------------------------------------------------------------------
     */
    @Override
    public Map<String,Object> getSearchBlogByKeyword(SearchBlogDto search) {
        Map<String,Object> rsltMap = new HashMap<>();
        Map<String,Object> connectResult = new HashMap<>();
        String searchWord = search.getSearchWord().trim(); // 검색어 PARAM
        String searchSort = search.getSearchSort().trim(); // 정렬 PARAM
        ISearchBlog searchBlog = null; //결과값 도출하기위한 다형성
        String isSuccess = "N";
        int pageNumber = 1; // 현재 페이지 PARAM
        int pageSize = 10; // 노출할 페이지 사이즈 PARAM
        if(search.getPageNumber() != null && 0 < search.getPageNumber()){
            pageNumber = search.getPageNumber();
        }
        if(search.getPageSize() != null && 0 < search.getPageSize()){
            pageSize = search.getPageSize();
        }
        // 요청 불량시 순차적 검색 API 사용 (KAKAO)
        if(!"S".equals(isSuccess)){
            //정렬 PRARAM Naver 에 맞게 가공
            KakaoSearchBlogDto kakaoSearchBlogDto = KakaoSearchBlogDto.builder()
                    .query(searchWord)
                    .sort(searchSort)
                    .page(pageNumber)
                    .size(pageSize)
                    .build();
            searchBlog = kakaoSearchBlog;
            connectResult = kakaoSearchBlog.apiConnect(kakaoSearchBlogDto);
            isSuccess = connectResult.get("status").toString();
        }

        // 순차적 검색 API 사용 (NAVER)
        if(!"S".equals(isSuccess)){
            if("recency".equals(searchSort)) searchSort = "date";
            NaverSearchBlogDto naverSearchBlogDto = NaverSearchBlogDto.builder()
                    .query(searchWord)
                    .sort(searchSort)
                    .start(pageNumber)
                    .display(pageSize)
                    .build();
            searchBlog = naverSearchBlog;
            connectResult = naverSearchBlog.apiConnect(naverSearchBlogDto);
            isSuccess = connectResult.get("status").toString();
        }

        rsltMap.put("status","ERROR");
        rsltMap.put("messgae",connectResult.get("message"));
        //GET 블로그 조회 결과
        if("S".equals(isSuccess)){
            SearchBlogDto.SearchBlogResult searchBlogResult = searchBlog.getSerchBlogResult();
            this.updateKewordForRDBMS(searchWord);
            //페이징 처리
            Page<SearchBlogDto.SearchBlogItem> pageing = new PageImpl<>(
                    searchBlogResult.getSearchBlogList(),
                    PageRequest.of(pageNumber - 1, pageSize),
                    searchBlogResult.getPageableCount()
            );
            rsltMap.put("status","SUCESS");
            rsltMap.put("data",pageing);
        }

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
