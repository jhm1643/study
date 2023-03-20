package com.kabang.batch.scheduler;

import com.kabang.batch.job.PopularKeywordJob;
import com.kabang.batch.job.SearchKeywordJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final JobLauncher jobLauncher;
    private final PopularKeywordJob popularKeywordJob;
    private final SearchKeywordJob searchKeywordJob;

    @Scheduled(cron = "${com.kabang.scheduling.cron.merge-popular-keyword}")
    public void mergePopularKeywordBatchStart() {
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(confMap);

        try {
            jobLauncher.run(popularKeywordJob.mergePopularKeywordJob(), jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e) {

            log.error("배치 수행 실패", e.getMessage());
        }
    }

    @Scheduled(cron = "${com.kabang.scheduling.cron.remove-search-keyword}")
    public void searchKeywordBatchStart() {
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(confMap);

        try {
            jobLauncher.run(searchKeywordJob.removeExpireDataJob(), jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e) {

            log.error("배치 수행 실패", e.getMessage());
        }
    }

}