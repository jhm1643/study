package com.kabang.domain.repository;

import com.kabang.domain.entity.PopularKeyword;
import com.kabang.domain.repository.querydsl.PopularKeywordRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PopularKeywordRepository extends JpaRepository<PopularKeyword, Long>, PopularKeywordRepositoryCustom {

    Optional<PopularKeyword> findByKeyword(String keyword);
}
