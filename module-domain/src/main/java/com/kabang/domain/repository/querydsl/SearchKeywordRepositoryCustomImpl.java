package com.kabang.domain.repository.querydsl;

import com.kabang.common.dto.SearchKeywordDto;
import com.kabang.common.dto.type.FlagType;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.kabang.domain.entity.QSearchKeyword.searchKeyword;

@RequiredArgsConstructor
public class SearchKeywordRepositoryCustomImpl implements SearchKeywordRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long countHitsKeywordByCheck(FlagType checkYn) {
        return queryFactory
                .select(searchKeyword.keyword)
                .from(searchKeyword)
                .where(searchKeyword.checkYn.eq(checkYn))
                .groupBy(searchKeyword.keyword)
                .stream().count();
    }

    @Override
    public List<SearchKeywordDto> findHitsKeywordByCheck(FlagType checkYn, long limit){
        return queryFactory
                .select(
                        Projections.fields(
                                SearchKeywordDto.class,
                                searchKeyword.keyword,
                                searchKeyword.count().as("hitsCount")
                        )
                ).from(searchKeyword)
                .where(searchKeyword.checkYn.eq(checkYn))
                .groupBy(searchKeyword.keyword)
                .limit(limit)
                .fetch();
    }

    @Override
    public List<SearchKeywordDto> findHitsKeywordByKeywords(List<String> keywords) {
        return queryFactory
                .select(
                        Projections.fields(
                                SearchKeywordDto.class,
                                searchKeyword.keyword,
                                searchKeyword.count().as("hitsCount")
                        )
                ).from(searchKeyword)
                .where(searchKeyword.keyword.in(keywords))
                .groupBy(searchKeyword.keyword)
                .fetch();
    }
}
