package com.kabang.batch.job;

import com.kabang.TestBatchConfig;
import com.kabang.common.dto.type.FlagType;
import com.kabang.domain.entity.SearchKeywordHistory;
import com.kabang.domain.repository.SearchKeywordHistoryRepository;
import com.kabang.domain.repository.SearchKeywordHitsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

@SpringBootTest(classes = {TestBatchConfig.class, AggregatePopularKeywordsJob.class})
@SpringBatchTest
@ActiveProfiles("test")
public class AggregatePopularKeywordsJobTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    SearchKeywordHistoryRepository searchKeywordHistoryRepository;

    @Autowired
    SearchKeywordHitsRepository searchKeywordHitsRepository;

    @Test
    public void 인기_키워드_집계_배치() throws Exception {
        //given
        var loopCount = 1000;
        var keyword1 = "아경";
        var keyword2 = "맛집";
        var keyword3 = "데이트";

        var threadCount = loopCount * 3;
        var executorService = Executors.newFixedThreadPool(threadCount);
        var latch = new CountDownLatch(threadCount);

        List<SearchKeywordHistory> searchKeywordHistoryList = new ArrayList<>();
        for (int i = 0; i < loopCount; i++) {
            var index = i;
            executorService.submit(() -> {
                searchKeywordHistoryList.add(SearchKeywordHistory.create(keyword1));
                searchKeywordHistoryList.add(SearchKeywordHistory.create(keyword1 + index));
                latch.countDown();
            });
        }
        for (int i = 0; i < loopCount; i++) {
            var index = i;
            executorService.submit(() -> {
                searchKeywordHistoryList.add(SearchKeywordHistory.create(keyword2));
                searchKeywordHistoryList.add(SearchKeywordHistory.create(keyword2));
                searchKeywordHistoryList.add(SearchKeywordHistory.create(keyword2 + index));
                latch.countDown();
            });
        }
        for (int i = 0; i < loopCount; i++) {
            var index = i;
            executorService.submit(() -> {
                searchKeywordHistoryList.add(SearchKeywordHistory.create(keyword3));
                searchKeywordHistoryList.add(SearchKeywordHistory.create(keyword3));
                searchKeywordHistoryList.add(SearchKeywordHistory.create(keyword3));
                searchKeywordHistoryList.add(SearchKeywordHistory.create(keyword3 + index));
                latch.countDown();
            });
        }

        latch.await();
        searchKeywordHistoryRepository.saveAll(searchKeywordHistoryList);

        //when
        jobLauncherTestUtils.launchJob(new JobParametersBuilder()
                .addString("DATE_TIME", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .toJobParameters());

        //then
        var keywordHitsDtos = searchKeywordHitsRepository.findPopularKeyword(10);

        //인기 순위 집계 확인
        assert keywordHitsDtos.size() == 10;
        assert keywordHitsDtos.get(0).getKeyword().equals(keyword3);
        assert keywordHitsDtos.get(0).getHitsCount() == loopCount * 3;

        var historyList = searchKeywordHistoryRepository.findAllByKeyword(keyword1);

        //집계된 데이터 여부 확인
        assert historyList.stream()
                .filter(e -> FlagType.Y == e.getCheckYn())
                .count() == historyList.size();

        //테스트 완료후 삭제
        searchKeywordHistoryRepository.deleteAll(searchKeywordHistoryList);
    }
}