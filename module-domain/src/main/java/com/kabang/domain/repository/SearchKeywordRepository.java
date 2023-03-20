package com.kabang.domain.repository;

import com.kabang.common.dto.type.FlagType;
import com.kabang.domain.entity.SearchKeyword;
import com.kabang.domain.repository.querydsl.SearchKeywordRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Long>, SearchKeywordRepositoryCustom {

    Boolean existsByAccessIdAndKeyword(String accessId, String keyword);
    List<SearchKeyword> findByKeywordIn(List<String> keywords);
    List<SearchKeyword> findByKeyword(String keyword);
    List<SearchKeyword> findByCheckYnAndCreateDtBefore(FlagType checkYn, LocalDateTime expireDate);
}
