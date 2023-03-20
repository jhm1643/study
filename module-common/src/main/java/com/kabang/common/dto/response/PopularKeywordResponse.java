package com.kabang.common.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ApiModel(value = "인기 검색어 목록")
public class PopularKeywordResponse {

    @ApiModelProperty("키워드")
    private String keyword;

    @ApiModelProperty("검색된 횟수")
    private long hitsCount;
}
