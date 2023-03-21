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
public class GetV1SearchNaverBlogDto {

    @ApiModelProperty("검색된 문서 수")
    private int total;

    @ApiModelProperty("겸색 결과 목록")
    private List<Item> items = new ArrayList<>();

    @Getter
    @Setter
    public static class Item {

        @ApiModelProperty("블로그 글 제목")
        private String title;

        @ApiModelProperty("블로그 글 요약")
        private String description;

        @ApiModelProperty("블로그 글 URL")
        private String link;

        @ApiModelProperty("블로그의 이름")
        private String bloggername;

        @ApiModelProperty("블로그 글 작성시간")
        @JsonDeserialize(using = CustomJsonConverter.LocalDateTimeDateDeserializer.class)
        private LocalDateTime postdate;
    }
}
