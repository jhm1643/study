package com.kabang.common.dto.request;

import com.kabang.common.dto.type.SortType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@ApiModel(value = "블로그 검색 요청")
public class BlogSearchRequest {

    @ApiModelProperty("keyword")
    private String keyword;

    @ApiModelProperty("정렬")
    private SortType sort = SortType.ACCURACY;

    @ApiModelProperty("페이지 번호")
    @Range(min = 1, max = 50, message = "페이지 번호 범위는 1 ~ 50 입니다.")
    private int page = 1;

    @ApiModelProperty("페이지 사이즈")
    @Range(min = 1, max = 50, message = "페이지 사이즈 범위는 1 ~ 50 입니다.")
    private int size = 10;
}
