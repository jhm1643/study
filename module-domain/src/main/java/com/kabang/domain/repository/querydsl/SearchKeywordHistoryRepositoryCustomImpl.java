package com.kabang.domain.repository.querydsl;

import com.kabang.common.dto.KeywordHitsDto;
import com.kabang.common.dto.type.FlagType;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.kabang.domain.entity.QSearchKeywordHistory.searchKeywordHistory;

@RequiredArgsConstructor
public class SearchKeywordHistoryRepositoryCustomImpl implements SearchKeywordHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long countHitsKeywordByCheck(FlagType checkYn) {
        return queryFactory
                .select(searchKeywordHistory.keyword)
                .from(searchKeywordHistory)
                .where(searchKeywordHistory.checkYn.eq(checkYn))
                .groupBy(searchKeywordHistory.keyword)
                .stream().count();
    }

    @Override
    public List<KeywordHitsDto> findHitsKeywordByCheck(FlagType checkYn, long limit){
        return queryFactory
                .select(
                        Projections.fields(
                                KeywordHitsDto.class,
                                searchKeywordHistory.keyword,
                                searchKeywordHistory.count().as("hitsCount")
                        )
                ).from(searchKeywordHistory)
                .where(searchKeywordHistory.checkYn.eq(checkYn))
                .groupBy(searchKeywordHistory.keyword)
                .limit(limit)
                .fetch();
    }

    @Override
    public List<Long> findIdsByCheckYnAndCreateDtBefore(FlagType checkYn, LocalDateTime expireDate, long limit) {
        return queryFactory
                .select(searchKeywordHistory.id)
                .from(searchKeywordHistory)
                .where(searchKeywordHistory.checkYn.eq(checkYn)
                        .and(searchKeywordHistory.createDt.before(expireDate))
                ).limit(limit)
                .fetch();
    }
}
