package com.kabang.external.client;

import com.kabang.common.dto.GetV1SearchNaverBlogDto;
import com.kabang.common.dto.GetV2SearchKakaoBlogDto;
import com.kabang.common.dto.request.BlogSearchRequest;
import com.kabang.common.dto.type.SortType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

@SpringBootTest
public class ExternalApiClientTest {

    @Autowired ExternalApiClient externalApiClient;

    @Test
    void 카카오_블로그_조회(){
        //given
        var request = new BlogSearchRequest();
        request.setKeyword("카카오");
        request.setPage(1);
        request.setSize(10);
        request.setSort(SortType.ACCURACY);

        //when
        var response = externalApiClient.getV2SearchKakaoBlog(request);

        //then
        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody().getDocuments().size() == request.getSize();
        for (GetV2SearchKakaoBlogDto.Document document : response.getBody().getDocuments()) {
            assert Objects.nonNull(document);
        }
    }

    @Test
    void 네이버_블로그_조회(){
        //given
        var request = new BlogSearchRequest();
        request.setKeyword("네이버");
        request.setPage(1);
        request.setSize(10);
        request.setSort(SortType.ACCURACY);

        //when
        var response = externalApiClient.getV1SearchNaverBlog(request);

        //then
        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody().getItems().size() == request.getSize();
        for (GetV1SearchNaverBlogDto.Item item : response.getBody().getItems()) {
            assert Objects.nonNull(item);
        }
    }
}
