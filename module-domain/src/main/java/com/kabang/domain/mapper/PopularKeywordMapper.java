package com.kabang.domain.mapper;

import com.kabang.common.dto.SearchKeywordDto;
import com.kabang.common.dto.response.PopularKeywordResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PopularKeywordMapper {

    PopularKeywordResponse toResponse(SearchKeywordDto dto);
}
