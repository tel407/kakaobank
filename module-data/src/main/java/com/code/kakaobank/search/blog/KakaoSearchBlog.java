package com.code.kakaobank.search.blog;

import com.code.kakaobank.common.util.ApiSupportUtil;
import com.code.kakaobank.search.payload.KakaoSearchBlogDto;
import com.code.kakaobank.search.payload.SearchBlogDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KakaoSearchBlog implements ISearchBlog<KakaoSearchBlogDto>{
    @Value("${kakao.api.host}") private String kakaoApiHost;
    @Value("${kakao.api.search.blog.url}") private String kakaoApiUrl;
    @Value("${kakao.api.key}") private String kakaoApiKey;
    public String moduleName = "KAKAO_BLOG_API";

    @Autowired
    private ApiSupportUtil ApiSupportUtil;
    private SearchBlogDto.SearchBlogResult searchBlogResult;

    @Override
    public Map<String, Object> validRequestParam(KakaoSearchBlogDto kakaoSearchBlogDto) throws Exception {
        Map<String,Object> rsltMap = new HashMap<>();
        rsltMap.put("status","S");
        if( null == kakaoSearchBlogDto.getQuery() || "".equals(kakaoSearchBlogDto.getQuery())){
            throw new Exception("검색어는(query) 필수값 입니다");
        }
        return rsltMap;
    }

    @Override
    public Map<String, Object> apiConnect(SearchBlogDto.SearchBlogRequestDto search){
        Map<String,Object> rsltMap = new HashMap<>();
        rsltMap.put("status","S");
        try {

            KakaoSearchBlogDto kakaoSearchBlogDto = KakaoSearchBlogDto.builder()
                    .query(search.getWord().trim())
                    .sort(search.getSort().trim())
                    .page(search.getPage())
                    .size(search.getCnt())
                    .build();

            //필수 PARAM 검사
            this.validRequestParam(kakaoSearchBlogDto);

            URL apiURL = new URL(this.kakaoApiHost + this.kakaoApiUrl);
            HttpsURLConnection offerConn = (HttpsURLConnection) apiURL.openConnection();
            offerConn.setRequestMethod("GET");
            offerConn.setRequestProperty("Authorization","KakaoAK "+this.kakaoApiKey);
            offerConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            offerConn.setConnectTimeout(6000);
            offerConn.setDoOutput(true);
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> mapParam = objectMapper.convertValue(kakaoSearchBlogDto, new TypeReference<Map<String, Object>>() {});

            DataOutputStream cpnOfferWr = new DataOutputStream(offerConn.getOutputStream());
            cpnOfferWr.writeBytes(ApiSupportUtil.paramToQueryString(mapParam));
            cpnOfferWr.flush();
            cpnOfferWr.close();

            JSONObject jObjectResponse = new JSONObject(ApiSupportUtil.handleResponse(offerConn));
            if(200 == offerConn.getResponseCode()) {
                this.convertResultDataByKaKao(jObjectResponse);
                rsltMap.put("message","SUCCESS :: API 호출 성공");
            }else{
                String aaa = jObjectResponse.get("message").toString();
                throw new Exception(jObjectResponse.get("message").toString());
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

    private void convertResultDataByKaKao(JSONObject jObjectResponse) throws Exception{
        // 데이터 가공
        JSONObject meta = jObjectResponse.getJSONObject("meta");
        JSONArray documentList =jObjectResponse.getJSONArray("documents");
        ArrayList<SearchBlogDto.SearchBlogItem> SearchBlogList = new ArrayList<>();
        // 블로그 검색 내용
        for (Object item :  documentList){
            JSONObject documentItem = (JSONObject) item;
            SearchBlogList.add(
                SearchBlogDto.SearchBlogItem.builder()
                    .title(documentItem.get("title").toString())
                    .contents(documentItem.get("contents").toString())
                    .url(documentItem.get("url").toString())
                    .blogname(documentItem.get("blogname").toString())
                    .thumbnail(documentItem.get("thumbnail").toString())
                    .datetime(documentItem.get("datetime").toString())
                    .build()
            );
        }

        SearchBlogDto.SearchBlogResult searchBlogResultData = SearchBlogDto.SearchBlogResult.builder()
                .totalCount((int)meta.get("total_count"))
                .pageableCount((int)meta.get("pageable_count"))
                .isEnd((boolean)meta.get("is_end"))
                .SearchBlogList(SearchBlogList)
                .build();

        this.searchBlogResult = searchBlogResultData;
    }
}
