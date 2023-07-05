package com.kabang.app.controller;

import com.kabang.app.service.BlogSearchApplicationService;
import com.kabang.common.dto.request.BlogSearchRequest;
import com.kabang.common.dto.response.BlogSearchResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/kabang/blog")
@Api(tags = "블로그")
@RequiredArgsConstructor
public class BlogContoller {

    private final BlogSearchApplicationService blogSearchApplicationService;

    @ApiOperation(value = "검색")
    @GetMapping("/search")
    public ResponseEntity<BlogSearchResponse> searchBlog(
            @Valid BlogSearchRequest request
    ){
        return ResponseEntity.ok(blogSearchApplicationService.findBlog(request));
    }
}
