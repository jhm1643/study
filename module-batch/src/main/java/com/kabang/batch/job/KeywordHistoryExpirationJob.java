package com.kabang.batch.job;

import com.kabang.batch.mapper.ItemReaderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Map;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class KeywordHistoryExpirationJob {

    @Value("${com.kabang.batch.search-keyword-store-expire-day}")
    private long searchKeywordStoreExpireDay;

    @Value("${com.kabang.batch.remove-expire-data-batch-size}")
    private int removeExpireDataBatchSize;

    private final DataSource dataSource;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job keywordHistoryExpirationBatchJob() throws Exception {
        return jobBuilderFactory.get("keywordHistoryExpirationBatchJob")
                .start(keywordHistoryExpirationBatchStep())
                .build();
    }

    @Bean
    public Step keywordHistoryExpirationBatchStep() throws Exception {
        return stepBuilderFactory.get("keywordHistoryExpirationBatchStep")
                .<Long, Long>chunk(removeExpireDataBatchSize)
                .reader(keywordHistoryExpirationBatchReader())
                .writer(keywordHistoryExpirationBatchWriter())
                .build();
    }

    @Bean
    public JdbcPagingItemReader<Long> keywordHistoryExpirationBatchReader() throws Exception {
        return new JdbcPagingItemReaderBuilder<Long>()
                .name("keywordHistoryExpirationBatchReader")
                .dataSource(dataSource)
                .rowMapper(new ItemReaderMapper.keywordHistoryExpirationReaderMapper())
                .pageSize(removeExpireDataBatchSize)
                .queryProvider(readerQuery())
                .parameterValues(Map.of("expireDay", LocalDateTime.now().minusDays(searchKeywordStoreExpireDay)))
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Long> keywordHistoryExpirationBatchWriter(){
        return new JdbcBatchItemWriterBuilder<Long>()
                .dataSource(dataSource)
                .sql("DELETE FROM search_keyword_history WHERE id = ?")
                .itemPreparedStatementSetter(((item, ps) -> ps.setLong(1, item)))
                .beanMapped()
                .build();
    }

    private PagingQueryProvider readerQuery() throws Exception {
        var queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("SELECT id");
        queryProvider.setFromClause("FROM search_keyword_history");
        queryProvider.setWhereClause("WHERE check_yn = 'Y' and create_dt <= :expireDay");
        queryProvider.setSortKey("id");
        return queryProvider.getObject();
    }
}
