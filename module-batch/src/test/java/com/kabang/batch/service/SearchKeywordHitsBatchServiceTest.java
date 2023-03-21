package com.kabang.batch.service;

import com.kabang.domain.repository.SearchKeywordHistoryHistoryRepository;
import com.kabang.domain.service.SearchKeywordHitsService;
import com.kabang.domain.service.SearchKeywordHistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
public class SearchKeywordHitsBatchServiceTest {

    @Value("${com.kabang.batch.search-keyword-store-expire-day}")
    private long searchKeywordStoreExpireDay;

    @Autowired
    SearchKeywordHistoryService searchKeywordHistoryService;
    @Autowired
    PopularKeywordBatchService popularKeywordBatchService;
    @Autowired
    SearchKeywordHitsService searchKeywordHitsService;
    @Autowired
    SearchKeywordHistoryHistoryRepository searchKeywordHistoryRepository;

    @Test
    void 인기검색어_마이그레이션_배치(){
        //given
        //총 11개의 검색어 생성
        createSearchKeyword();

        //when
        popularKeywordBatchService.mergePopularKeyword();

        //then
        var response = searchKeywordHitsService.findTop10PopularKeyword();
        assert response.size() == 10;

        //조회수 1위 확인
        assert response.get(0).getKeyword().equals("카카오11");
        assert response.get(0).getHitsCount() == 11;

        //조회수 10위 확인
        assert response.get(9).getKeyword().equals("카카오2");
        assert response.get(9).getHitsCount() == 2;
    }

    @Test
    void 검색_키워드_삭제_배치(){
        //given

        //검색 키워드 생성
        createSearchKeyword();

        //인기 검색어 마이그레이션 진행
        popularKeywordBatchService.mergePopularKeyword();

        //삭제 배치 기준 날짜 적용
        searchKeywordHistoryRepository.findAll().stream()
                .forEach(searchKeyword -> searchKeyword.updateCreateDt(LocalDateTime.now().minusDays(searchKeywordStoreExpireDay)));

        //when
        popularKeywordBatchService.removeExpireData();

        //then
        assert searchKeywordHistoryRepository.findAll().isEmpty();
    }

    private void createSearchKeyword(){
        int k = 11;
        while (k > 0){
            for (int i = 0; i < k; i++) {
                searchKeywordHistoryService.createSearchKeyword(String.valueOf(i), "카카오" + k);
            }
            k--;
        }
    }
}