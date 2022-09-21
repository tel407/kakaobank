package com.code.kakaobank.search.payload;


import com.code.kakaobank.search.entity.SearchKeywordScore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Builder
@Getter
public class SearchRankDto {
    private String keyword;   // 검색을 원하는 질의어
    private int score;    //결과 문서 정렬 방식
    private int rank;   // 검색을 원하는 질의어

}
