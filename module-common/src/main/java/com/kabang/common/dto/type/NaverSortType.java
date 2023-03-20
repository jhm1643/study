package com.kabang.common.dto.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NaverSortType {

    SIM("정확도순으로 내림차순 정렬"),
    DATE("날짜순으로 내림차순 정렬");

    private String description;
}
