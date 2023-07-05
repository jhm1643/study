package com.kabang.batch.job;

import com.kabang.TestBatchConfig;
import com.kabang.common.dto.type.FlagType;
import com.kabang.domain.entity.SearchKeywordHistory;
import com.kabang.domain.repository.SearchKeywordHistoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = {TestBatchConfig.class, KeywordHistoryExpirationJob.class})
@SpringBatchTest
@ActiveProfiles("test")
public class KeywordHistoryExpirationJobTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    SearchKeywordHistoryRepository searchKeywordHistoryRepository;

    @Test
    void 히스토리_삭제_배치() throws Exception {
        //given
        var keyword = "데이트";

        List<SearchKeywordHistory> searchKeywordHistoryList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            var searchKeywordHistory = SearchKeywordHistory.create(keyword + i);
            //체크 값 Y로 셋팅
            searchKeywordHistory.updateCheck(FlagType.Y);
            searchKeywordHistoryList.add(searchKeywordHistory);
        }
        searchKeywordHistoryRepository.saveAll(searchKeywordHistoryList);

        //배치 전 데이터 유무 확인
        assert searchKeywordHistoryRepository.findHitsKeywordByCheck(FlagType.Y, 10).size() > 0;

        //when
        jobLauncherTestUtils.launchJob();

        //then
        //데이터 삭제 확인
        assert 0 == searchKeywordHistoryRepository.findHitsKeywordByCheck(FlagType.Y, 10).size();

    }
}