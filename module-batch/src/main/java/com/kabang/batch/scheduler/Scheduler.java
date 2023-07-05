package com.kabang.batch.scheduler;

import com.kabang.batch.job.AggregatePopularKeywordsJob;
import com.kabang.batch.job.KeywordHistoryExpirationJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final JobLauncher jobLauncher;
    private final AggregatePopularKeywordsJob aggregatePopularKeywordsJob;
    private final KeywordHistoryExpirationJob keywordHistoryExpirationJob;

    //10초 마다
    @Scheduled(cron = "${com.kabang.scheduling.cron.aggregate-popular-keywords-batch}")
    public void keywordHitsCountBatchStart() throws Exception {
        try {
            jobLauncher.run(aggregatePopularKeywordsJob.aggregatePopularKeywordsBatchJob(), new JobParametersBuilder()
                    .addString("DATE_TIME", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e) {

            log.error("배치 수행 실패", e.getMessage());
        }
    }

    //20초 마다
    @Scheduled(cron = "${com.kabang.scheduling.cron.keyword-history-expiration-batch}")
    public void keywordHistoryExpirationBatchStart() throws Exception {;
        try {
            jobLauncher.run(keywordHistoryExpirationJob.keywordHistoryExpirationBatchJob(), new JobParametersBuilder()
                    .addString("DATE_TIME", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e) {

            log.error("배치 수행 실패", e.getMessage());
        }
    }

}