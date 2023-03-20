package com.kabang.external.config;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Value("${api.external.kakao.domain}")
    private String kakaoApiDomain;

    @Value("${api.external.kakao.auth-key}")
    private String kakaoApiAuthKey;

    @Value("${api.external.naver.domain}")
    private String naverApiDomain;

    @Value("${api.external.naver.client-id}")
    private String naverApiClientId;

    @Value("${api.external.naver.client-secret}")
    private String naverApiClientSecret;


    @Bean
    public RestTemplate kakaoAuthRestTemplate(){
        var httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(100)    //최대 오픈되는 커넥션 수
                .setMaxConnPerRoute(5)   //IP, 포트 1쌍에 대해 수행할 커넥션 수
                .setDefaultHeaders(List.of(new BasicHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoApiAuthKey)))
                .build();

        var factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(5000);       //읽기시간초과, ms
        factory.setConnectTimeout(3000);    //연결시간초과, ms
        factory.setHttpClient(httpClient);

        var restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(factory);
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(kakaoApiDomain));

        return restTemplate;
    }

    @Bean
    public RestTemplate naverAuthRestTemplate(){
        var authHeader = List.of(
                new BasicHeader("X-Naver-Client-Id", naverApiClientId),
                new BasicHeader("X-Naver-Client-Secret", naverApiClientSecret)
        );
        var httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(100)    //최대 오픈되는 커넥션 수
                .setMaxConnPerRoute(5)   //IP, 포트 1쌍에 대해 수행할 커넥션 수
                .setDefaultHeaders(authHeader)
                .build();

        var factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(5000);       //읽기시간초과, ms
        factory.setConnectTimeout(3000);    //연결시간초과, ms
        factory.setHttpClient(httpClient);

        var restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(factory);
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(naverApiDomain));

        return restTemplate;
    }
}
