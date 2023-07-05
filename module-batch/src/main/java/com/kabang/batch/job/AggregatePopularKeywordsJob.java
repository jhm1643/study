package com.kabang.batch.job;

import com.kabang.batch.dto.AggregatePopularKeywordsDto;
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
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AggregatePopularKeywordsJob {

    @Value("${com.kabang.batch.aggregate-popular-keywords.chunk-size}")
    private int batchChunkSize;

    @Value("${com.kabang.batch.aggregate-popular-keywords.page-size}")
    private int batchPageSize;

    private final DataSource dataSource;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job aggregatePopularKeywordsBatchJob() throws Exception{
        return jobBuilderFactory.get("aggregatePopularKeywordsJob")
                .start(aggregatePopularKeywordsStep())
                .build();
    }

    @Bean
    public Step aggregatePopularKeywordsStep() throws Exception{
        return stepBuilderFactory.get("aggregatePopularKeywordsStep")
                .<AggregatePopularKeywordsDto, AggregatePopularKeywordsDto> chunk(batchChunkSize)
                .reader(aggregatePopularKeywordsReader())
                .writer(aggregatePopularKeywordsCompositeWriter())
                .build();
    }

    @Bean
    public JdbcPagingItemReader<AggregatePopularKeywordsDto> aggregatePopularKeywordsReader() throws Exception{
        return new JdbcPagingItemReaderBuilder<AggregatePopularKeywordsDto>()
                .name("aggregatePopularKeywordsReader")
                .dataSource(dataSource)
                .rowMapper(new ItemReaderMapper.KeywordHitsCountBatchReaderMapper())
                .pageSize(batchPageSize)
                .queryProvider(readerQuery())
                .build();
    }

    @Bean
    public CompositeItemWriter aggregatePopularKeywordsCompositeWriter(){
        return new CompositeItemWriterBuilder<AggregatePopularKeywordsDto>()
                .delegates(List.of(aggregatePopularKeywordsWriter1(), aggregatePopularKeywordsWriter2()))
                .build();
    }

    @Bean
    //조회된 키워드는 다음 배치에 조회되지 않도록 check_yn 값을 Y로 변경
    public JdbcBatchItemWriter<AggregatePopularKeywordsDto> aggregatePopularKeywordsWriter1(){
        return new JdbcBatchItemWriterBuilder<AggregatePopularKeywordsDto>()
                .dataSource(dataSource)
                .sql("UPDATE search_keyword_history SET check_yn = 'Y'" +
                        "WHERE keyword = :keyword")
                .beanMapped()
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<AggregatePopularKeywordsDto> aggregatePopularKeywordsWriter2(){
        return new JdbcBatchItemWriterBuilder<AggregatePopularKeywordsDto>()
                .dataSource(dataSource)
                .sql("INSERT INTO search_keyword_hits(id, keyword, hits_count) " +
                        "VALUES( NEXTVAL('search_keyword_hits_seq'), :keyword, :hitsCount) " +
                        "ON DUPLICATE KEY UPDATE hits_count = hits_count + :hitsCount")
                .beanMapped()
                .build();
    }

    private PagingQueryProvider readerQuery() throws Exception {
        var queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("SELECT keyword, COUNT(1) AS hits_count");
        queryProvider.setFromClause("FROM search_keyword_history");
        queryProvider.setWhereClause("WHERE check_yn = 'N'");
        queryProvider.setGroupClause("GROUP BY keyword");
        queryProvider.setSortKey("keyword");
        return queryProvider.getObject();
    }
}
