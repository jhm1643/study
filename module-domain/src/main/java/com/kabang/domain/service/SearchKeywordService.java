package com.kabang.domain.service;

import com.kabang.domain.entity.SearchKeyword;
import com.kabang.domain.repository.SearchKeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchKeywordService {

    private final SearchKeywordRepository searchKeywordRepository;

    @Transactional
    @Async
    public void createSearchKeyword(String accessId, String keyword){
        if(keyword.contains(" ")) {
            keyword = keyword.split(" ")[1];
        }

        final String realKeyword = keyword;
        var existSearchKeyword = searchKeywordRepository.existsByAccessIdAndKeyword(accessId, keyword);
        if(existSearchKeyword == false){
            searchKeywordRepository.save(SearchKeyword.create(accessId, realKeyword));
        }
    }
}
