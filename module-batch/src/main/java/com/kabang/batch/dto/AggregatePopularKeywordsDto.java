package com.kabang.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AggregatePopularKeywordsDto {

    private String keyword;
    private long hitsCount;
}
