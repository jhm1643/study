package com.kabang.batch.service;

import com.kabang.common.dto.type.FlagType;
import com.kabang.domain.entity.PopularKeyword;
import com.kabang.domain.repository.PopularKeywordRepository;
import com.kabang.domain.repository.SearchKeywordRepository;
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

    private final PopularKeywordRepository popularKeywordRepository;
    private final SearchKeywordRepository searchKeywordRepository;

    @Transactional
    public void mergePopularKeyword(){
        long count = searchKeywordRepository.countHitsKeywordByCheck(FlagType.N);

        for (long i = 0; i <= count; i++) {

            if(count == 0){
                break;
            }else if(count < 500){
                i = count;
            }else if(count - i < 500){
                i = count - i;
            }else{
                i += 500;
            }

            searchKeywordRepository.findHitsKeywordByCheck(FlagType.N, i).stream()
                    .forEach(dto -> {
                        popularKeywordRepository.findByKeyword(dto.getKeyword())
                                .ifPresentOrElse(
                                        popularKeyword -> popularKeyword.updateHitCount(dto.getHitsCount()),
                                        () -> popularKeywordRepository.save(PopularKeyword.create(dto.getKeyword(), dto.getHitsCount())));

                        searchKeywordRepository.findByKeyword(dto.getKeyword()).stream()
                                .forEach(searchKeyword -> searchKeyword.updateCheck(FlagType.Y));

                        searchKeywordRepository.flush();
                        popularKeywordRepository.flush();
                    });
        }
    }

    public void removeExpireData(){
        searchKeywordRepository.findByCheckYnAndCreateDtBefore(FlagType.Y, LocalDateTime.now().minusDays(searchKeywordStoreExpireDay));
    }
}
