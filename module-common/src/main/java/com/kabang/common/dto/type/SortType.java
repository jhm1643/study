package com.kabang.common.dto.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SortType {

    ACCURACY("정확도순", NaverSortType.SIM),
    RECENCY("최신순", NaverSortType.DATE);

    private String description;
    private NaverSortType naverSortType;
}
