package com.kabang.batch.service;

import com.kabang.common.dto.KeywordHitsDto;
import com.kabang.common.dto.type.FlagType;
import com.kabang.domain.entity.SearchKeywordHits;
import com.kabang.domain.repository.SearchKeywordHitsRepository;
import com.kabang.domain.repository.SearchKeywordHistoryHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PopularKeywordBatchService {

    @Value("${com.kabang.batch.search-keyword-store-expire-day}")
    private long searchKeywordStoreExpireDay;

    @Value("${com.kabang.batch.merge-popular-keyword-batch-size}")
    private long mergePopularKeywordBatchSize;

    @Value("${com.kabang.batch.remove-expire-data-batch-size}")
    private long removeExpireDataBatchSize;

    private final SearchKeywordHitsRepository searchKeywordHitsRepository;
    private final SearchKeywordHistoryHistoryRepository searchKeywordHistoryRepository;

    @Transactional
    public void mergePopularKeyword(){
        long count = searchKeywordHistoryRepository.countHitsKeywordByCheck(FlagType.N);
        for (long i = 0; i <= count; i++) {

            if(count == 0){
                break;
            }else if(count < mergePopularKeywordBatchSize){
                i = count;
            }else if(count - i < mergePopularKeywordBatchSize){
                i = count - i;
            }else{
                i += mergePopularKeywordBatchSize;
            }

            for (KeywordHitsDto keywordHitsDto : searchKeywordHistoryRepository.findHitsKeywordByCheck(FlagType.N, i)) {
                searchKeywordHitsRepository.findByKeyword(keywordHitsDto.getKeyword())
                        .ifPresentOrElse(
                                searchKeywordHits -> searchKeywordHits.updateHitCount(keywordHitsDto.getHitsCount()),
                                () -> searchKeywordHitsRepository.save(SearchKeywordHits.create(keywordHitsDto.getKeyword(), keywordHitsDto.getHitsCount())));

                searchKeywordHistoryRepository.findByKeyword(keywordHitsDto.getKeyword()).stream()
                        .forEach(searchKeyword -> searchKeyword.updateCheck(FlagType.Y));

                searchKeywordHistoryRepository.flush();
                searchKeywordHitsRepository.flush();
            }
        }
    }

    @Transactional
    public void removeExpireData(){
        while(true){
            var searchKeywordHistoryIds = searchKeywordHistoryRepository.findIdsByCheckYnAndCreateDtBefore(FlagType.Y, LocalDateTime.now().minusDays(searchKeywordStoreExpireDay), removeExpireDataBatchSize);
            if(searchKeywordHistoryIds.isEmpty()){
                break;
            }else{
                searchKeywordHistoryRepository.deleteAllByIdInBatch(searchKeywordHistoryIds);
            }
        }
    }
}
