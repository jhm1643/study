package com.kabang.domain.repository;

import com.kabang.domain.entity.SearchKeywordHistory;
import com.kabang.domain.repository.querydsl.SearchKeywordHistoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SearchKeywordHistoryHistoryRepository extends JpaRepository<SearchKeywordHistory, Long>, SearchKeywordHistoryRepositoryCustom {

    Boolean existsByAccessIdAndKeyword(String accessId, String keyword);
    List<SearchKeywordHistory> findByKeyword(String keyword);
    Optional<SearchKeywordHistory> findByAccessIdAndKeyword(String accessId, String keyword);
}
