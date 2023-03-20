package com.kabang.batch.job;

import com.kabang.batch.service.PopularKeywordBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SearchKeywordJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final PopularKeywordBatchService popularKeywordBatchService;

    @Bean
    public Job removeExpireDataJob(){
        return jobBuilderFactory.get("removeExpireDataJob")
                .start(removeExpireDataStep1()) //STEP 1 실행
                .build();
    }

    @Bean
    public Step removeExpireDataStep1(){
        return stepBuilderFactory.get("removeExpireDataStep1")
                .tasklet((stepContribution, chunkContext) -> {
                    popularKeywordBatchService.removeExpireData();
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
