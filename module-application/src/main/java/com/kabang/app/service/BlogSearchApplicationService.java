package com.kabang.app.service;

import com.kabang.common.dto.request.BlogSearchRequest;
import com.kabang.common.dto.response.BlogSearchResponse;
import com.kabang.common.exception.ApiException;
import com.kabang.common.exception.ApiExceptionCode;
import com.kabang.domain.mapper.BlogKakaoMapper;
import com.kabang.domain.service.SearchKeywordHistoryService;
import com.kabang.external.client.ExternalApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlogSearchApplicationService {

    private final ExternalApiClient externalApiClient;
    private final SearchKeywordHistoryService searchKeywordHistoryService;

    private final BlogKakaoMapper blogKakaoMapper;

    public BlogSearchResponse findBlog(BlogSearchRequest request){
        BlogSearchResponse response = null;
        var kakaoApiResponse = externalApiClient.getV2SearchKakaoBlog(request);

        if(kakaoApiResponse.getStatusCode().is2xxSuccessful()){
            response = blogKakaoMapper.toResponse(kakaoApiResponse.getBody());
        }else{
            var naverApiResponse = externalApiClient.getV1SearchNaverBlog(request);
            if(naverApiResponse.getStatusCode().is2xxSuccessful()){
                response = blogKakaoMapper.toResponse(naverApiResponse.getBody());
            }else{
                throw new ApiException(ApiExceptionCode.EXTERNAL_API_ERROR);
            }
        }

        searchKeywordHistoryService.saveSearchKeywordHistory(request.getKeyword());
        return response;
    }
}
