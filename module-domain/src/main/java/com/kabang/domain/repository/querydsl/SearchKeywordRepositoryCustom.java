package com.kabang.domain.repository.querydsl;

import com.kabang.common.dto.SearchKeywordDto;
import com.kabang.common.dto.type.FlagType;

import java.util.List;

public interface SearchKeywordRepositoryCustom {

    Long countHitsKeywordByCheck(FlagType checkYn);
    List<SearchKeywordDto> findHitsKeywordByCheck(FlagType checkYn, long limit);
    List<SearchKeywordDto> findHitsKeywordByKeywords(List<String> keywords);
}
