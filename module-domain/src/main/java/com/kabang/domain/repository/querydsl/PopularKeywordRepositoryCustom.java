package com.kabang.domain.repository.querydsl;

import com.kabang.common.dto.SearchKeywordDto;

import java.util.List;

public interface PopularKeywordRepositoryCustom {

    List<SearchKeywordDto> findPopularKeyword(long limit);
}
