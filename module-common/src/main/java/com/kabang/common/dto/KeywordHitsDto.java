package com.kabang.common.dto;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class KeywordHitsDto {

    private String keyword;
    private long hitsCount;
}
