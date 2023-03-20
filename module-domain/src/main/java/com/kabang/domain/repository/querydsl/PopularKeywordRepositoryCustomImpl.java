package com.kabang.domain.repository.querydsl;

import com.kabang.common.dto.SearchKeywordDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.kabang.domain.entity.QPopularKeyword.popularKeyword;

@RequiredArgsConstructor
public class PopularKeywordRepositoryCustomImpl implements PopularKeywordRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SearchKeywordDto> findPopularKeyword(long limit) {
        return jpaQueryFactory
                .select(
                        Projections.fields(
                                SearchKeywordDto.class,
                                popularKeyword.keyword,
                                popularKeyword.hitsCount
                        )
                )
                .from(popularKeyword)
                .orderBy(popularKeyword.hitsCount.desc())
                .limit(limit)
                .fetch();
    }
}
