package com.kabang.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "카카오 블로그 검색 결과")
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogSearchResponse implements Serializable {

    @ApiModelProperty("페이징 정보")
    private Meta meta;

    @ApiModelProperty("겸색 결과 목록")
    private List<Document> documents = new ArrayList<>();

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Meta implements Serializable {

        @ApiModelProperty("검색된 문서 수")
        private Integer totalCount;

        @ApiModelProperty("total_count중 노출 가능 문서 수")
        private Integer pageableCount;

        @ApiModelProperty("마지막 페이지 여부")
        private Boolean isEnd;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Document implements Serializable {

        @ApiModelProperty("블로그 글 제목")
        private String title;

        @ApiModelProperty("블로그 글 요약")
        private String contents;

        @ApiModelProperty("블로그 글 URL")
        private String url;

        @ApiModelProperty("블로그의 이름")
        private String blogName;

        @ApiModelProperty("검색 시스템에서 추출한 대표 미리보기 이미지 URL, 미리보기 크기 및 화질은 변경될 수 있음")
        private String thumbnail;

        @ApiModelProperty("블로그 글 작성시간, ISO 8601")
        private LocalDateTime dateTime;
    }
}
