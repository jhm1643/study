package com.kabang.domain.service;

import com.kabang.common.dto.response.PopularKeywordResponse;
import com.kabang.domain.mapper.PopularKeywordMapper;
import com.kabang.domain.repository.SearchKeywordHitsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchKeywordHitsService {

    private final SearchKeywordHitsRepository searchKeywordHitsRepository;
    private final PopularKeywordMapper popularKeywordMapper;

    public List<PopularKeywordResponse> findTop10PopularKeyword(){
        return searchKeywordHitsRepository.findPopularKeyword(10).stream()
                .map(popularKeywordMapper::toResponse)
                .collect(Collectors.toList());
    }
}
