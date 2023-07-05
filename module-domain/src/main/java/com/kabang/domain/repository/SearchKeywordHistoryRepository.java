package com.kabang.domain.repository;

import com.kabang.domain.entity.SearchKeywordHistory;
import com.kabang.domain.repository.querydsl.SearchKeywordHistoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchKeywordHistoryRepository extends JpaRepository<SearchKeywordHistory, Long>, SearchKeywordHistoryRepositoryCustom {

    List<SearchKeywordHistory> findAllByKeyword(String keyword);
}
