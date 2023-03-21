package com.kabang.domain.repository;

import com.kabang.domain.entity.SearchKeywordHits;
import com.kabang.domain.repository.querydsl.SearchKeywordHitsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SearchKeywordHitsRepository extends JpaRepository<SearchKeywordHits, Long>, SearchKeywordHitsRepositoryCustom {

    Optional<SearchKeywordHits> findByKeyword(String keyword);
}
