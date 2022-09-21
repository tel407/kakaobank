package com.code.kakaobank.search.test;

import com.code.kakaobank.search.TestConfigration;
import com.code.kakaobank.search.entity.SearchKeywordScore;
import com.code.kakaobank.search.payload.SearchBlogDto;
import com.code.kakaobank.search.repository.SearchKeywordScoreiRepository;
import com.code.kakaobank.search.service.SearchBlogServiceTest;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(classes = TestConfigration.class)
class ModuleDataTest {

    @Autowired
    private SearchBlogServiceTest searchBlogServiceTest;
    @Autowired
    private SearchKeywordScoreiRepository searchKeywordScoreiRepository;

    @Test
    @DisplayName("페이지 PARAM validataion")
    void ValidPageParam() {
        System.out.println("## ST : validPageParam ##");

        SearchBlogDto.SearchBlogRequestDto searchParam = new SearchBlogDto.SearchBlogRequestDto();
        searchParam.setPage(-1);
        searchParam.setCnt(0);

        SearchBlogDto.SearchBlogRequestDto validResult = searchBlogServiceTest.validPageParam(searchParam);
        Assert.assertEquals(1,(int)validResult.getPage());
        Assert.assertEquals(10,(int)validResult.getCnt());
        System.out.println("## ED : validPageParam ##");
    }

    @Test
    @DisplayName("카카오 API 에러시 Naver API 호출")
    void ModuleErrorNextModulPlay() {
        System.out.println("## ST : ModuleErrorNextModulPlay ##");

        SearchBlogDto.SearchBlogRequestDto searchParam = new SearchBlogDto.SearchBlogRequestDto();
        searchParam.setWord("전화승 화이팅");
        searchParam.setPage(-1);
        searchParam.setCnt(0);

        Map<String,Object> callSearchBlogApiModuleResult = searchBlogServiceTest.callSearchBlogApiModule(searchParam);

        Assert.assertEquals("NAVER_BLOG_API",(String)callSearchBlogApiModuleResult.get("moduleName"));
        System.out.println("## ED : ModuleErrorNextModulPlay ##");
    }

    @Test
    @DisplayName("검색시 키워드 조회 후 카운트 업")
    void UpdateKewordForRDBMS() {
        System.out.println("## ST : UpdateKewordForRDBMS ##");
        String keyword = "파이썬";
        int loopNum = 3;
        System.out.println("@@@@@@@@@ 증가 예상 치   :   " + loopNum);
        List<SearchKeywordScore> keywordListPrev = searchKeywordScoreiRepository.findByKeywordInOrderByScoreDesc(Arrays.asList(new String[]{ keyword }));
        int scorePrev =  keywordListPrev.get(0).getScore();
        System.out.println("@@@@@@@@@  scorePrev   :   " + scorePrev);

        for(int i = 0; i < loopNum; i++){
            searchBlogServiceTest.updateKewordForRDBMS(keyword);
        }

        List<SearchKeywordScore> keywordListAfter = searchKeywordScoreiRepository.findByKeywordInOrderByScoreDesc(Arrays.asList(new String[]{ keyword }));
        int scoreAfter =  keywordListAfter.get(0).getScore();
        System.out.println("@@@@@@@@@  scoreAfter   :   " + scoreAfter);
        Assert.assertEquals((scorePrev + loopNum),scoreAfter);
        System.out.println("## ED : UpdateKewordForRDBMS ##");
    }


}
