package com.code.kakaobank.search.entity;

import com.code.kakaobank.search.payload.SearchRankDto;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@DynamicInsert
@RequiredArgsConstructor
@Table(name="searchkeywordscore")
public class SearchKeywordScore {

    @Id
    private String keyword;

    @Column(columnDefinition = "integer default 1")
    private int score;


    @Builder
    public SearchKeywordScore (String keyword, int score){
        this.keyword = keyword;
        this.score = score;
    }

}
