package com.kabang.app.controller;

import com.kabang.common.dto.response.PopularKeywordResponse;
import com.kabang.domain.service.SearchKeywordHitsService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/kabang/popular-keyword")
@Api(tags = "인기 검색어")
@RequiredArgsConstructor
public class PopularKeywordController {

    private final SearchKeywordHitsService searchKeywordHitsService;

    @GetMapping("/search/blog")
    public ResponseEntity<List<PopularKeywordResponse>> findBlogTop10PopularKeywords(){
        return ResponseEntity.ok(searchKeywordHitsService.findTop10PopularKeyword());
    }
}
