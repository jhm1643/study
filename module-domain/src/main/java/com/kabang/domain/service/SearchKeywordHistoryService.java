package com.kabang.domain.service;

import com.kabang.domain.entity.SearchKeywordHistory;
import com.kabang.domain.repository.SearchKeywordHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchKeywordHistoryService {

    private final SearchKeywordHistoryRepository searchKeywordHistoryRepository;

    @Transactional
    @Async
    public void saveSearchKeywordHistory(String keyword){
        if(keyword.contains(" ")) {
            keyword = keyword.split(" ")[1];
        }

        final String realKeyword = keyword;
        searchKeywordHistoryRepository.save(SearchKeywordHistory.create(realKeyword));
    }
}
