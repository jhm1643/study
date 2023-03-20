package com.kabang.common.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kabang.common.converter.CustomJsonConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GetV2SearchKakaoBlogDto {

    @ApiModelProperty("meta")
    private Meta meta;

    @ApiModelProperty("겸색 결과 목록")
    private List<Document> documents = new ArrayList<>();

    @Setter
    @Getter
    public static class Meta {

        @ApiModelProperty("검색된 문서 수")
        private int total_count;

        @ApiModelProperty("total_count중 노출 가능 문서 수")
        private int pageable_count;

        @ApiModelProperty("마지막 페이지 여부")
        private Boolean is_end;
    }

    @Getter
    @Setter
    public static class Document {

        @ApiModelProperty("블로그 글 제목")
        private String title;

        @ApiModelProperty("블로그 글 요약")
        private String contents;

        @ApiModelProperty("블로그 글 URL")
        private String url;

        @ApiModelProperty("블로그의 이름")
        private String blogname;

        @ApiModelProperty("검색 시스템에서 추출한 대표 미리보기 이미지 URL, 미리보기 크기 및 화질은 변경될 수 있음")
        private String thumbnail;

        @ApiModelProperty("블로그 글 작성시간")
        @JsonDeserialize(using = CustomJsonConverter.LocalDateTimeDateTimeDeserializer.class)
        private LocalDateTime datetime;
    }
}
