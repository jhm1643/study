package com.kabang.domain.service;

import com.kabang.domain.repository.SearchKeywordHistoryHistoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

@SpringBootTest
@Transactional
public class SearchKeywordHistoryServiceTest {

    @Autowired
    SearchKeywordHistoryService searchKeywordHistoryService;
    @Autowired
    SearchKeywordHistoryHistoryRepository searchKeywordHistoryRepository;

    @Test
    void 검색_키워드_생성(){
        //given
        var accessId = "1";
        var keyword = "카카오";

        //when
        searchKeywordHistoryService.createSearchKeyword(accessId, keyword);

        //then
        assert searchKeywordHistoryRepository.findByAccessIdAndKeyword(accessId, keyword).isPresent();
    }

    @Test
    void 검색_키워드_생성_동시성_테스트() throws InterruptedException{
        //given
        var threadCount = 200;
        var executorService = Executors.newFixedThreadPool(threadCount);
        var latch = new CountDownLatch (threadCount);

        /*
         * 1. 200명의 사용자가 동시에 동일한 키워드를 검색
         * 2. 동일한 사용자가 동일한 키워드 입력 불가능(어뷰징 방지)
         */
        for (int i = 1; i <= threadCount; i++) {
            final String accessId = String.valueOf(i);
            executorService.execute(() -> {
                searchKeywordHistoryService.createSearchKeyword(accessId, "카카오");
                latch.countDown();
            });
        }

        latch.await();

        assert searchKeywordHistoryRepository.findAll().size() == threadCount;
    }

}
