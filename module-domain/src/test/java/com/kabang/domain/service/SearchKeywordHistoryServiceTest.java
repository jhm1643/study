package com.kabang.domain.service;

import com.kabang.domain.repository.SearchKeywordHistoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class SearchKeywordHistoryServiceTest {

    @Autowired
    SearchKeywordHistoryService searchKeywordHistoryService;
    @Autowired
    SearchKeywordHistoryRepository searchKeywordHistoryRepository;

    @Test
    void 검색_키워드_생성(){
        //given
        var keyword = "카카오";

        //when
        searchKeywordHistoryService.saveSearchKeywordHistory(keyword);

        //then
        assert searchKeywordHistoryRepository.findAllByKeyword(keyword).isEmpty() == false;
    }
}
