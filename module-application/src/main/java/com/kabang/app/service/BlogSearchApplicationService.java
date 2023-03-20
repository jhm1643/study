package com.kabang.app.service;

import com.kabang.external.client.ExternalApiClient;
import com.kabang.common.dto.request.BlogSearchRequest;
import com.kabang.common.dto.response.BlogSearchResponse;
import com.kabang.common.exception.ApiException;
import com.kabang.common.exception.ApiExceptionCode;
import com.kabang.domain.mapper.BlogKakaoMapper;
import com.kabang.domain.service.SearchKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogSearchApplicationService {

    private final ExternalApiClient externalApiClient;
    private final SearchKeywordService searchKeywordService;

    private final BlogKakaoMapper blogKakaoMapper;

    public BlogSearchResponse findBlog(String accessId, BlogSearchRequest request){
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

        searchKeywordService.createSearchKeyword(accessId, request.getKeyword());
        return response;
    }
}
