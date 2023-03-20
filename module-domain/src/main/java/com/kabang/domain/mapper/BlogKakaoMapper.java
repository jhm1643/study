package com.kabang.domain.mapper;

import com.kabang.common.dto.GetV1SearchNaverBlogDto;
import com.kabang.common.dto.GetV2SearchKakaoBlogDto;
import com.kabang.common.dto.response.BlogSearchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BlogKakaoMapper {

    BlogSearchResponse toResponse(GetV2SearchKakaoBlogDto dto);

    @Mapping(target = "meta.totalCount", source = "dto.total")
    @Mapping(target = "documents", source = "dto.items")
    BlogSearchResponse toResponse(GetV1SearchNaverBlogDto dto);

    @Mapping(target = "totalCount", source = "total_count")
    @Mapping(target = "pageableCount", source = "pageable_count")
    @Mapping(target = "isEnd", source = "is_end")
    BlogSearchResponse.Meta toResponse(GetV2SearchKakaoBlogDto.Meta meta);

    @Mapping(target = "blogName", source = "blogname")
    @Mapping(target = "dateTime", source = "datetime")
    BlogSearchResponse.Document toDocument(GetV2SearchKakaoBlogDto.Document document);

    @Mapping(target = "contents", source = "description")
    @Mapping(target = "url", source = "link")
    @Mapping(target = "blogName", source = "bloggername")
    @Mapping(target = "dateTime", source = "postdate")
    BlogSearchResponse.Document toDocument(GetV1SearchNaverBlogDto.Item item);
}
