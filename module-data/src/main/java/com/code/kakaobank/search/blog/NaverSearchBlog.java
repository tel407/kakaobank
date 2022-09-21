package com.code.kakaobank.search.blog;

import com.code.kakaobank.common.util.ApiSupportUtil;
import com.code.kakaobank.search.payload.NaverSearchBlogDto;
import com.code.kakaobank.search.payload.SearchBlogDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class NaverSearchBlog implements ISearchBlog<NaverSearchBlogDto>{
    @Value("${naver.api.host}") private String naverApiHost;
    @Value("${naver.api.search.blog.url}") private String naverApiUrl;
    @Value("${naver.api.client.id}") private String naverApiClientId;
    @Value("${naver.api.client.secret}") private String naverApiClientSecret;
    @Autowired
    private ApiSupportUtil ApiSupportUtil;
    private SearchBlogDto.SearchBlogResult searchBlogResult;

    @Override
    public Map<String, Object> validRequestParam(NaverSearchBlogDto naverSearchBlogDto) throws Exception {
        Map<String,Object> rsltMap = new HashMap<>();
        rsltMap.put("status","S");
        if( null == naverSearchBlogDto.getQuery() || "".equals(naverSearchBlogDto.getQuery())){
            throw new Exception("검색어는(query) 필수값 입니다");
        }
        return rsltMap;
    }

    @Override
    public Map<String, Object> apiConnect(NaverSearchBlogDto naverSearchBlogDto){
        Map<String,Object> rsltMap = new HashMap<>();
        rsltMap.put("status","S");
        try {

            //필수 PARAM 검사
            this.validRequestParam(naverSearchBlogDto);

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> mapParam = objectMapper.convertValue(naverSearchBlogDto, new TypeReference<Map<String, Object>>() {});

            URL apiURL = new URL(this.naverApiHost + this.naverApiUrl+"?"+ ApiSupportUtil.paramToQueryString(mapParam));
            HttpsURLConnection offerConn = (HttpsURLConnection) apiURL.openConnection();
            offerConn.setRequestMethod("GET");
            offerConn.setRequestProperty("X-Naver-Client-Id",this.naverApiClientId);
            offerConn.setRequestProperty("X-Naver-Client-Secret",this.naverApiClientSecret);
            offerConn.setConnectTimeout(6000);
            offerConn.setDoOutput(true);

            JSONObject jObjectResponse = new JSONObject(ApiSupportUtil.handleResponse(offerConn));
            if(200 == offerConn.getResponseCode()) {
                this.convertResultDataByNaver(jObjectResponse);
                rsltMap.put("message","SUCCESS :: API 호출 성공");
            }else{
                throw new Exception(jObjectResponse.get("errorMessage").toString());
            }
        } catch (Exception e) {
            rsltMap.put("status","E");
            rsltMap.put("message","ERROR :: API 호출 커넥션 에러 = " + e.getMessage());
        }
        return rsltMap;
    }

    @Override
    public SearchBlogDto.SearchBlogResult getSerchBlogResult() {
        return searchBlogResult;
    }

    private void convertResultDataByNaver(JSONObject jObjectResponse) throws Exception{
        // 데이터 가공
        JSONArray documentList =jObjectResponse.getJSONArray("items");
        ArrayList<SearchBlogDto.SearchBlogItem> SearchBlogList = new ArrayList<>();
        // 블로그 검색 내용
        for (Object item :  documentList){
            JSONObject documentItem = (JSONObject) item;
            SearchBlogList.add(
                SearchBlogDto.SearchBlogItem.builder()
                    .title(documentItem.get("title").toString())
                    .contents(documentItem.get("description").toString())
                    .url(documentItem.get("link").toString())
                    .blogname(documentItem.get("bloggername").toString())
                    .thumbnail("")
                    .datetime(documentItem.get("postdate").toString())
                    .build()
            );
        }

        SearchBlogDto.SearchBlogResult searchBlogResultData = SearchBlogDto.SearchBlogResult.builder()
                .totalCount((int)jObjectResponse.get("total"))
                .pageableCount((int)jObjectResponse.get("total"))
                .isEnd(false)
                .SearchBlogList(SearchBlogList)
                .build();

        this.searchBlogResult = searchBlogResultData;
    }
}
