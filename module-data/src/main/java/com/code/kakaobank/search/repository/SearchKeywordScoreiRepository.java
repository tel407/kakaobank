package com.code.kakaobank.search.repository;

import com.code.kakaobank.search.entity.SearchKeywordScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;


@Repository
public interface SearchKeywordScoreiRepository extends JpaRepository<SearchKeywordScore, String> {

    /*동시성 문제*/
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "1000")})
    @Transactional
    List<SearchKeywordScore> findByKeywordInOrderByScoreDesc(List<String> keword);
    List<SearchKeywordScore> findTop10ByOrderByScoreDesc();
}
