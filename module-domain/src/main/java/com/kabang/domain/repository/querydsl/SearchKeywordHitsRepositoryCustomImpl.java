package com.kabang.domain.repository.querydsl;

import com.kabang.common.dto.KeywordHitsDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.kabang.domain.entity.QSearchKeywordHits.searchKeywordHits;


@RequiredArgsConstructor
public class SearchKeywordHitsRepositoryCustomImpl implements SearchKeywordHitsRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<KeywordHitsDto> findPopularKeyword(long limit) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                KeywordHitsDto.class,
                                searchKeywordHits.keyword,
                                searchKeywordHits.hitsCount
                        )
                )
                .from(searchKeywordHits)
                .orderBy(searchKeywordHits.hitsCount.desc())
                .limit(limit)
                .fetch();
    }
}
