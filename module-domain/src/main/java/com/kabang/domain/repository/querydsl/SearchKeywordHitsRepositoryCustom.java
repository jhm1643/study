package com.kabang.domain.repository.querydsl;

import com.kabang.common.dto.KeywordHitsDto;

import java.util.List;

public interface SearchKeywordHitsRepositoryCustom {

    List<KeywordHitsDto> findPopularKeyword(long limit);
}
