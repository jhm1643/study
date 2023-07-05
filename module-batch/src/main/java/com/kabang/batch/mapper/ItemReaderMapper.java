package com.kabang.batch.mapper;

import com.kabang.batch.dto.AggregatePopularKeywordsDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ItemReaderMapper {

    public static class KeywordHitsCountBatchReaderMapper implements RowMapper<AggregatePopularKeywordsDto> {
        @Override
        public AggregatePopularKeywordsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return AggregatePopularKeywordsDto.builder()
                    .keyword(rs.getString("keyword"))
                    .hitsCount(rs.getLong("hits_count"))
                    .build();
        }
    }

    public static class keywordHistoryExpirationReaderMapper implements RowMapper<Long> {
        @Override
        public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getLong("id");
        }
    }

}
