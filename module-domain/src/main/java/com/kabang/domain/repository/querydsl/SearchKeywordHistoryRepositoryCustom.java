package com.kabang.domain.repository.querydsl;

import com.kabang.common.dto.KeywordHitsDto;
import com.kabang.common.dto.type.FlagType;

import java.time.LocalDateTime;
import java.util.List;

public interface SearchKeywordHistoryRepositoryCustom {

    Long countHitsKeywordByCheck(FlagType checkYn);
    List<KeywordHitsDto> findHitsKeywordByCheck(FlagType checkYn, long limit);
    List<Long> findIdsByCheckYnAndCreateDtBefore(FlagType checkYn, LocalDateTime expireDate, long limit);
}
