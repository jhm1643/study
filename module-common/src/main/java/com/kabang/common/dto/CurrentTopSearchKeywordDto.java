package com.kabang.common.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CurrentTopSearchKeywordDto {

    private String keyword;
    private long hits;
}
